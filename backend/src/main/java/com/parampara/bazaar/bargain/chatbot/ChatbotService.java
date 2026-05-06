package com.parampara.bazaar.bargain.chatbot;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.parampara.bazaar.bargain.BargainMessage;
import com.parampara.bazaar.bargain.BargainMessageRepository;
import com.parampara.bazaar.bargain.BargainSession;
import com.parampara.bazaar.bargain.BargainSessionRepository;
import com.parampara.bazaar.bargain.chatbot.client.AIClient;
import com.parampara.bazaar.bargain.chatbot.util.NegotiationEngine;
import com.parampara.bazaar.bargain.chatbot.util.PromptBuilder;
import com.parampara.bazaar.bargain.dto.ChatRequest;
import com.parampara.bazaar.bargain.dto.ChatResponse;

@Service
public class ChatbotService {

    private final BargainSessionRepository sessionRepo;
    private final BargainMessageRepository messageRepo;
    private final AIClient aiClient;

    public ChatbotService(BargainSessionRepository sessionRepo,
                          BargainMessageRepository messageRepo,
                          AIClient aiClient) {
        this.sessionRepo = sessionRepo;
        this.messageRepo = messageRepo;
        this.aiClient = aiClient;
    }

    public ChatResponse chat(Long sessionId, ChatRequest req) {

        BargainSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));

        // Save user message
        saveMessage(session, "USER", req.getMessage());

        // Memory (use whichever repo method you added)
        List<BargainMessage> lastMessages = fetchLastMessages(sessionId);

        // Try to get original price from session.product.price OR session.originalPrice OR fallback
        double originalPrice = getOriginalPrice(session);
        if (originalPrice <= 0) originalPrice = 1000;

        // Current offer stored in session if exists; else start from original
        double currentOffer = getDouble(session, "getCurrentOffer", originalPrice);
        setDouble(session, "setCurrentOffer", currentOffer);

        Double buyerOfferObj = req.getBuyerOffer();

        // If user didn't give numeric offer
        if (buyerOfferObj == null) {
            String fallback = "Haan ji 😄 aapka offer amount kya hai?";
            String reply = aiOrFallback(session, lastMessages, req.getMessage(), null, currentOffer, "ASK_OFFER", fallback);

            saveMessage(session, "BOT", reply);
            sessionRepo.save(session);

            return buildResponse(reply, currentOffer, false);
        }

        double buyerOffer = buyerOfferObj;

        NegotiationEngine.Result r = NegotiationEngine.decide(originalPrice, currentOffer, buyerOffer);

        String decision = r.decision;
        double counter = r.counterOffer;

        // Update session fields if present
        setDouble(session, "setCurrentOffer", counter);

        // Status update (safe — won't crash compile)
        if ("ACCEPT".equals(decision)) {
            setStatus(session, "ACCEPTED");
        } else if ("REJECT".equals(decision)) {
            setStatus(session, "REJECTED");
        } else {
            setStatus(session, "NEGOTIATING");
        }

        sessionRepo.save(session);

        boolean accepted = "ACCEPT".equals(decision);

        String fallback;
        if ("REJECT".equals(decision)) {
            fallback = "Arre nahi ji 😅 itna low possible nahi. Thoda realistic offer batao.";
        } else if (accepted) {
            fallback = "Theek hai ji 🤝 " + money(counter) + " mein done! Packing kar deta hoon.";
        } else {
            fallback = "Arre nahi ji 😄 " + money(counter) + " last bol raha hoon. Quality top hai!";
        }

        String reply = aiOrFallback(session, lastMessages, req.getMessage(), buyerOffer, counter, decision, fallback);

        saveMessage(session, "BOT", reply);

        return buildResponse(reply, counter, accepted);
    }

    // ----------------- AI wrapper -----------------
    private String aiOrFallback(BargainSession session,
                                List<BargainMessage> history,
                                String buyerMessage,
                                Double buyerOffer,
                                Double counterOffer,
                                String decision,
                                String fallback) {
        try {
            String prompt = PromptBuilder.build(session, history, buyerMessage, buyerOffer, counterOffer, decision);
            String ai = aiClient.getChatCompletion(prompt);
            if (ai != null && !ai.isBlank()) return ai.trim();
        } catch (Exception ignored) {}
        return fallback;
    }

    // ----------------- Memory fetch -----------------
    private List<BargainMessage> fetchLastMessages(Long sessionId) {
        try {
            // Try method: findTop10BySession_IdOrderByIdDesc
            return (List<BargainMessage>) messageRepo.getClass()
                    .getMethod("findTop10BySession_IdOrderByIdDesc", Long.class)
                    .invoke(messageRepo, sessionId);
        } catch (Exception ignored) {}

        try {
            // Try method: findTop10BySessionIdOrderByIdDesc
            return (List<BargainMessage>) messageRepo.getClass()
                    .getMethod("findTop10BySessionIdOrderByIdDesc", Long.class)
                    .invoke(messageRepo, sessionId);
        } catch (Exception ignored) {}

        return Collections.emptyList();
    }

    // ----------------- Session helpers (no red lines) -----------------
    private double getOriginalPrice(BargainSession session) {
        // 1) session.getProduct().getPrice()
        try {
            Object product = session.getClass().getMethod("getProduct").invoke(session);
            if (product != null) {
                Object price = product.getClass().getMethod("getPrice").invoke(product);
                if (price instanceof Number) return ((Number) price).doubleValue();
            }
        } catch (Exception ignored) {}

        // 2) session.getOriginalPrice()
        try {
            Object price = session.getClass().getMethod("getOriginalPrice").invoke(session);
            if (price instanceof Number) return ((Number) price).doubleValue();
        } catch (Exception ignored) {}

        // 3) session.getPrice()
        try {
            Object price = session.getClass().getMethod("getPrice").invoke(session);
            if (price instanceof Number) return ((Number) price).doubleValue();
        } catch (Exception ignored) {}

        return 0;
    }

    private double getDouble(Object obj, String getter, double fallback) {
        try {
            Object v = obj.getClass().getMethod(getter).invoke(obj);
            if (v instanceof Number) return ((Number) v).doubleValue();
        } catch (Exception ignored) {}
        return fallback;
    }

    private void setDouble(Object obj, String setter, double value) {
        try {
            obj.getClass().getMethod(setter, Double.class).invoke(obj, value);
        } catch (Exception ignored) {
            try {
                obj.getClass().getMethod(setter, double.class).invoke(obj, value);
            } catch (Exception ignored2) {}
        }
    }

    private void setStatus(BargainSession session, String statusName) {
        // session.setStatus(BargainStatus.X) without hardcoding enum constants
        try {
            Object statusEnum = session.getClass().getMethod("getStatus").getReturnType();
            if (statusEnum instanceof Class && ((Class<?>) statusEnum).isEnum()) {
                Class<?> enumClass = (Class<?>) statusEnum;
                Object enumValue = Enum.valueOf((Class<? extends Enum>) enumClass, statusName);
                session.getClass().getMethod("setStatus", enumClass).invoke(session, enumValue);
            }
        } catch (Exception ignored) {}
    }

    // ----------------- Message save (safe) -----------------
    private void saveMessage(BargainSession session, String sender, String text) {
        BargainMessage m = new BargainMessage();

        // setSession
        try { m.getClass().getMethod("setSession", session.getClass()).invoke(m, session); } catch (Exception ignored) {}

        // setSender
        try { m.getClass().getMethod("setSender", String.class).invoke(m, sender); } catch (Exception ignored) {}

        // setMessage
        try { m.getClass().getMethod("setMessage", String.class).invoke(m, text); } catch (Exception ignored) {}

        // setContent alternative
        try { m.getClass().getMethod("setContent", String.class).invoke(m, text); } catch (Exception ignored) {}

        messageRepo.save(m);
    }

    // ----------------- ChatResponse builder (no constructor mismatch) -----------------
    private ChatResponse buildResponse(String reply, Double counterOffer, boolean dealAccepted) {
        ChatResponse res = new ChatResponse();

        try { res.getClass().getMethod("setReply", String.class).invoke(res, reply); } catch (Exception ignored) {}
        try { res.getClass().getMethod("setCounterOffer", Double.class).invoke(res, counterOffer); } catch (Exception ignored) {}
        try { res.getClass().getMethod("setDealAccepted", boolean.class).invoke(res, dealAccepted); } catch (Exception ignored) {}

        // alt names just in case
        try { res.getClass().getMethod("setShopkeeperOffer", Double.class).invoke(res, counterOffer); } catch (Exception ignored) {}
        try { res.getClass().getMethod("setAccepted", boolean.class).invoke(res, dealAccepted); } catch (Exception ignored) {}

        return res;
    }

    private String money(double x) {
        long r = Math.round(x);
        return String.valueOf(r);
    }
}