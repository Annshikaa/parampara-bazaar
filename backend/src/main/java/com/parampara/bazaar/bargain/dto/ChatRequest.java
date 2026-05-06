package com.parampara.bazaar.bargain.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequest {

    @NotBlank
    private String message;

    // optional buyer offer amount (ex: 600)
    private Double buyerOffer;

    public ChatRequest() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Double getBuyerOffer() { return buyerOffer; }
    public void setBuyerOffer(Double buyerOffer) { this.buyerOffer = buyerOffer; }

}
