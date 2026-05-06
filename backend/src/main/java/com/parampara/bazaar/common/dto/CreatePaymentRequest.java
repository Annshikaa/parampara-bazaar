package com.parampara.bazaar.common.dto;

import jakarta.validation.constraints.NotNull;

public class CreatePaymentRequest {

    @NotNull
    private Long orderId;

    public CreatePaymentRequest() {}

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}

