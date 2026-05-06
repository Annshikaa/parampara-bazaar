package com.parampara.bazaar.vendor;

import com.parampara.bazaar.common.enums.VendorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VendorProfileRequest {

    @NotBlank
    private String shopName;

    @NotBlank
    private String location;

    @NotNull
    private VendorType vendorType;

    public VendorProfileRequest() {}

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public VendorType getVendorType() { return vendorType; }
    public void setVendorType(VendorType vendorType) { this.vendorType = vendorType; }
}
