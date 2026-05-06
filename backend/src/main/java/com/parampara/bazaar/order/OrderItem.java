package com.parampara.bazaar.order;

import com.parampara.bazaar.common.enums.OrderItemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Double originalPrice;

    @Column(nullable = false)
    private Double minPrice;

    @Column(nullable = false)
    private Double finalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.parampara.bazaar.common.enums.OrderItemStatus itemStatus =
        com.parampara.bazaar.common.enums.OrderItemStatus.PLACED;


    private Long vendorId;

    @Column(nullable = false)
    private Integer quantity;

    public OrderItem() {}

    public Long getId() { return id; }

    public OrderItemStatus getItemStatus() { return itemStatus; }
    public void setItemStatus(OrderItemStatus itemStatus) { this.itemStatus = itemStatus; }

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }


    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
