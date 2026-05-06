package com.parampara.bazaar.bargain.dto;

public class ChatResponse {

    private boolean success;
    private String reply;
    private Double shopkeeperOffer;
    private String status;

    public ChatResponse() {}

    public ChatResponse(boolean success, String reply, Double shopkeeperOffer, String status) {
        this.success = success;
        this.reply = reply;
        this.shopkeeperOffer = shopkeeperOffer;
        this.status = status;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public Double getShopkeeperOffer() { return shopkeeperOffer; }
    public void setShopkeeperOffer(Double shopkeeperOffer) { this.shopkeeperOffer = shopkeeperOffer; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
