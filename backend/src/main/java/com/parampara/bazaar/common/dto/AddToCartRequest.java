package com.parampara.bazaar.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddToCartRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    // OPTIONAL: if buyer has a bargain session, pass it
    private Long bargainSessionId;

    public AddToCartRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getBargainSessionId() { return bargainSessionId; }
    public void setBargainSessionId(Long bargainSessionId) { this.bargainSessionId = bargainSessionId; }
}
