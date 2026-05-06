package com.parampara.bazaar.bargain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parampara.bazaar.bargain.dto.BargainResponse;
import com.parampara.bazaar.bargain.dto.ChatRequest;
import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.product.Product;
import com.parampara.bazaar.product.ProductRepository;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;

@Service
public class BargainService {

    private final BargainSessionRepository sessionRepo;
    private final BargainMessageRepository messageRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public BargainService(BargainSessionRepository sessionRepo,
                          BargainMessageRepository messageRepo,
                          ProductRepository productRepo,
                          UserRepository userRepo) {
        this.sessionRepo = sessionRepo;
        this.messageRepo = messageRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public BargainResponse startSession(Long productId) {
        User buyer = currentUser();

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // initial offer: slightly below original (like a vendor starting discount)
        double initialOffer = roundToNearest10(product.getOriginalPrice() * 0.95);

        // ensure not below min
        if (initialOffer < product.getMinPrice()) initialOffer = product.getOriginalPrice();

        BargainSession s = new BargainSession();
        s.setBuyer(buyer);
        s.setProduct(product);
        s.setStatus(BargainStatus.ACTIVE);
        s.setCurrentOffer(initialOffer);
        s.setTurns(0);
        s.setUpdatedAt(LocalDateTime.now());

        BargainSession saved = sessionRepo.save(s);

        // Bot opening message
        addMessage(saved, "BOT",
                "Namaste! 😊 For \"" + product.getName() + "\" I can start at ₹" + (int)Math.round(initialOffer) +
                        ". Tell me your best price!", initialOffer);

        return toResponse(saved.getId());
    }

    @Transactional
    public BargainResponse chat(Long sessionId, ChatRequest req) {
        User buyer = currentUser();

        BargainSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getBuyer().getId().equals(buyer.getId())) throw new RuntimeException("Forbidden");
        if (session.getStatus() != BargainStatus.ACTIVE) throw new RuntimeException("Session is not active");

        Product product = session.getProduct();

        // Save buyer message
        addMessage(session, "BUYER", req.getMessage(), req.getBuyerOffer());

        session.setTurns(session.getTurns() + 1);
        session.setUpdatedAt(LocalDateTime.now());

        double buyerOffer = req.getBuyerOffer() == null ? -1 : req.getBuyerOffer();
        double botOffer = computeBotOffer(session.getCurrentOffer(), buyerOffer, product.getMinPrice());

        // if buyer offers >= botOffer => accept buyerOffer (or botOffer)
        if (buyerOffer > 0 && buyerOffer >= botOffer) {
            session.setStatus(BargainStatus.ACCEPTED);
            session.setFinalPrice(buyerOffer);
            session.setCurrentOffer(buyerOffer);

            sessionRepo.save(session);

            addMessage(session, "BOT",
                    "Done deal 🤝! Final price locked at ₹" + (int)Math.round(buyerOffer) +
                            ". Proceed to checkout whenever you're ready.", buyerOffer);

            return toResponse(sessionId);
        }

        // Otherwise counter offer
        session.setCurrentOffer(botOffer);
        sessionRepo.save(session);

        String botText = makeBotText(req.getMessage(), buyerOffer, botOffer, product.getMinPrice());
        addMessage(session, "BOT", botText, botOffer);

        return toResponse(sessionId);
    }

    @Transactional
    public BargainResponse accept(Long sessionId) {
        User buyer = currentUser();

        BargainSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getBuyer().getId().equals(buyer.getId())) throw new RuntimeException("Forbidden");
        if (session.getStatus() != BargainStatus.ACTIVE) throw new RuntimeException("Session is not active");

        session.setStatus(BargainStatus.ACCEPTED);
        session.setFinalPrice(session.getCurrentOffer());
        session.setUpdatedAt(LocalDateTime.now());
        sessionRepo.save(session);

        addMessage(session, "BOT",
                "Great! Final price locked at ₹" + (int)Math.round(session.getFinalPrice()) +
                        ". Proceed to checkout.", session.getFinalPrice());

        return toResponse(sessionId);
    }

    public BargainResponse getSession(Long sessionId) {
        return toResponse(sessionId);
    }

    private BargainResponse toResponse(Long sessionId) {
        BargainSession s = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        List<BargainMessage> msgs = messageRepo.findTop10BySessionIdOrderByCreatedAtDesc(sessionId);

        return new BargainResponse(
                s.getId(),
                s.getProduct().getId(),
                s.getStatus(),
                s.getCurrentOffer(),
                s.getFinalPrice(),
                s.getTurns(),
                msgs
        );
    }

    private void addMessage(BargainSession session, String sender, String message, Double offer) {
        BargainMessage m = new BargainMessage();
        m.setSession(session);
        m.setSender(sender);
        m.setMessage(message);
        m.setOffer(offer);
        messageRepo.save(m);
    }

    // --- “AI-ish” bargain logic ---
    private double computeBotOffer(double currentOffer, double buyerOffer, double minPrice) {
        double nextOffer;

        if (buyerOffer > 0) {
            // Move part way towards buyer offer, but not all the way
            // Example: current 950, buyer 600 => next ~ 800
            nextOffer = currentOffer - ((currentOffer - buyerOffer) * 0.45);
        } else {
            // buyer didn't give number, small discount
            nextOffer = currentOffer * 0.97;
        }

        nextOffer = roundToNearest10(nextOffer);

        // Never go below min price
        if (nextOffer < minPrice) nextOffer = roundToNearest10(minPrice);

        // Never increase above current
        if (nextOffer > currentOffer) nextOffer = currentOffer;

        return nextOffer;
    }

    private String makeBotText(String buyerMsg, double buyerOffer, double botOffer, double minPrice) {
        String msgLower = buyerMsg == null ? "" : buyerMsg.toLowerCase(Locale.ROOT);

        if (buyerOffer > 0 && buyerOffer < minPrice) {
            return "I understand 😊 but I can't go below ₹" + (int)Math.round(minPrice) +
                    ". How about ₹" + (int)Math.round(botOffer) + "?";
        }

        if (msgLower.contains("last") || msgLower.contains("final")) {
            return "This is my best offer 🙏: ₹" + (int)Math.round(botOffer) +
                    ". If you confirm, I’ll lock it.";
        }

        return "I can do ₹" + (int)Math.round(botOffer) +
                ". If you accept, I'll lock this price for checkout 🤝";
    }

    private double roundToNearest10(double value) {
        return Math.round(value / 10.0) * 10.0;
    }
}
