package com.parampara.bazaar.vendor;

import com.parampara.bazaar.common.enums.VendorType;

public class VendorProfileResponse {

    private Long id;
    private String shopName;
    private String location;
    private VendorType vendorType;
    private Long userId;
    private String userEmail;

    public VendorProfileResponse() {}

    public VendorProfileResponse(Long id, String shopName, String location, VendorType vendorType, Long userId, String userEmail) {
        this.id = id;
        this.shopName = shopName;
        this.location = location;
        this.vendorType = vendorType;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public Long getId() { return id; }
    public String getShopName() { return shopName; }
    public String getLocation() { return location; }
    public VendorType getVendorType() { return vendorType; }
    public Long getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
}
