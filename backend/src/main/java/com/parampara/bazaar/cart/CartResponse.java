package com.parampara.bazaar.cart;

import java.util.List;

public class CartResponse {

    private List<CartItem> items;
    private double totalOriginalPrice;
    private double totalMinPrice;

    public CartResponse() {}

    public CartResponse(List<CartItem> items, double totalOriginalPrice, double totalMinPrice) {
        this.items = items;
        this.totalOriginalPrice = totalOriginalPrice;
        this.totalMinPrice = totalMinPrice;
    }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public double getTotalOriginalPrice() { return totalOriginalPrice; }
    public void setTotalOriginalPrice(double totalOriginalPrice) { this.totalOriginalPrice = totalOriginalPrice; }

    public double getTotalMinPrice() { return totalMinPrice; }
    public void setTotalMinPrice(double totalMinPrice) { this.totalMinPrice = totalMinPrice; }
}
