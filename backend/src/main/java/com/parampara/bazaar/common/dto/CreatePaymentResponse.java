package com.parampara.bazaar.common.dto;

public class CreatePaymentResponse {

    private String razorpayOrderId;
    private String keyId;
    private Double amount;
    private String currency;

    public CreatePaymentResponse() {}

    public CreatePaymentResponse(String razorpayOrderId, String keyId, Double amount, String currency) {
        this.razorpayOrderId = razorpayOrderId;
        this.keyId = keyId;
        this.amount = amount;
        this.currency = currency;
    }

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
