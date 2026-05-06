package com.parampara.bazaar.bargain.dto;

import jakarta.validation.constraints.NotNull;

public class StartBargainRequest {

    @NotNull
    private Long productId;

    public StartBargainRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}