package com.parampara.bazaar.bargain.chatbot.util;

import java.util.List;

import com.parampara.bazaar.bargain.BargainMessage;
import com.parampara.bazaar.bargain.BargainSession;

public class PromptBuilder {

    private PromptBuilder() {}

    public static String build(
            BargainSession session,
            List<BargainMessage> lastMessages,
            String buyerMessage,
            Double buyerOffer,
            Double counterOffer,
            String decision
    ) {
        StringBuilder history = new StringBuilder();
        if (lastMessages != null) {
            for (int i = lastMessages.size() - 1; i >= 0; i--) {
                BargainMessage m = lastMessages.get(i);
                String sender = safeGetString(m, "getSender", "SENDER");
                String text = safeGetString(m, "getMessage", "");
                history.append(sender).append(": ").append(text).append("\n");
            }
        }

        return """
        You are a traditional Indian bazaar shopkeeper.
        Speak in Hinglish, polite but firm, like a real seller.
        Keep replies short (1-2 lines), human, slightly witty.

        Decision: %s
        BuyerOffer: %s
        CounterOffer: %s

        Conversation so far:
        %s

        Buyer now says: "%s"

        Output ONLY the shopkeeper reply text (no JSON).
        """.formatted(
                decision,
                buyerOffer == null ? "NA" : buyerOffer,
                counterOffer == null ? "NA" : counterOffer,
                history.toString(),
                buyerMessage
        );
    }

    private static String safeGetString(Object obj, String method, String fallback) {
        try {
            Object v = obj.getClass().getMethod(method).invoke(obj);
            return v == null ? fallback : v.toString();
        } catch (Exception e) {
            return fallback;
        }
    }
}