package com.parampara.bazaar.bargain.chatbot.util;

public class NegotiationEngine {

    public static class Result {
        public final String decision; // ACCEPT / REJECT / COUNTER
        public final double counterOffer;

        public Result(String decision, double counterOffer) {
            this.decision = decision;
            this.counterOffer = counterOffer;
        }
    }

    private NegotiationEngine() {}

    public static Result decide(double originalPrice, double currentOffer, double buyerOffer) {

        // Floors/ceilings (tune these later)
        double minPrice = roundTo5(originalPrice * 0.75);      // shopkeeper won't go below 75%
        double rejectFloor = roundTo5(originalPrice * 0.50);   // insult offer below 50%

        // Hard stops
        if (buyerOffer < rejectFloor) {
            return new Result("REJECT", currentOffer);
        }

        // Accept if buyer is at/above min
        if (buyerOffer >= minPrice) {
            return new Result("ACCEPT", buyerOffer);
        }

        // Counter: move partway toward buyer, but not below minPrice
        // Step down 35% of the gap from currentOffer to buyerOffer
        double counter = currentOffer - ((currentOffer - buyerOffer) * 0.35);

        if (counter < minPrice) counter = minPrice;

        counter = roundTo5(counter);
        return new Result("COUNTER", counter);
    }

    private static double roundTo5(double x) {
        return Math.round(x / 5.0) * 5.0;
    }
}