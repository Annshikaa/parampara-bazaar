package com.parampara.bazaar.bargain.dto;

import com.parampara.bazaar.bargain.BargainMessage;
import com.parampara.bazaar.bargain.BargainStatus;

import java.util.List;


public class BargainResponse {

    private Long sessionId;
    private Long productId;
    private BargainStatus status;
    private Double currentOffer;
    private Double finalPrice;
    private Integer turns;
    private List<BargainMessage> messages;

    public BargainResponse() {}

    public BargainResponse(Long sessionId, Long productId, BargainStatus status,
                           Double currentOffer, Double finalPrice, Integer turns,
                           List<BargainMessage> messages) {
        this.sessionId = sessionId;
        this.productId = productId;
        this.status = status;
        this.currentOffer = currentOffer;
        this.finalPrice = finalPrice;
        this.turns = turns;
        this.messages = messages;
    }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public BargainStatus getStatus() { return status; }
    public void setStatus(BargainStatus status) { this.status = status; }

    public Double getCurrentOffer() { return currentOffer; }
    public void setCurrentOffer(Double currentOffer) { this.currentOffer = currentOffer; }

    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }

    public Integer getTurns() { return turns; }
    public void setTurns(Integer turns) { this.turns = turns; }

    public List<BargainMessage> getMessages() { return messages; }
    public void setMessages(List<BargainMessage> messages) { this.messages = messages; }
} 