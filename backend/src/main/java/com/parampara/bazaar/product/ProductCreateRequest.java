package com.parampara.bazaar.product;

import com.parampara.bazaar.common.enums.VendorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductCreateRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Double originalPrice;

    @NotNull
    private Double minPrice;

    @NotBlank
    private String category;

    private String imageUrl;

    @NotNull
    private VendorType vendorType;

    @NotBlank
    private String placeOrMarket;

    public ProductCreateRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public VendorType getVendorType() { return vendorType; }
    public void setVendorType(VendorType vendorType) { this.vendorType = vendorType; }

    public String getPlaceOrMarket() { return placeOrMarket; }
    public void setPlaceOrMarket(String placeOrMarket) { this.placeOrMarket = placeOrMarket; }
}
