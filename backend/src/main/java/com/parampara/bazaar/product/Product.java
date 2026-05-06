package com.parampara.bazaar.product;

import com.parampara.bazaar.common.enums.VendorType;
import com.parampara.bazaar.vendor.VendorProfile;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double originalPrice;

    @Column(nullable = false)
    private Double minPrice; // minimum vendor will accept after bargaining

    @Column(nullable = false)
    private String category;

    private String imageUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vendor_id")
    private VendorProfile vendor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorType vendorType;

    // e.g. "Sarojini Nagar" or "Banaras"
    @Column(nullable = false)
    private String placeOrMarket;

    public Product() {}

    public Long getId() { return id; }

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

    public VendorProfile getVendor() { return vendor; }
    public void setVendor(VendorProfile vendor) { this.vendor = vendor; }

    public VendorType getVendorType() { return vendorType; }
    public void setVendorType(VendorType vendorType) { this.vendorType = vendorType; }

    public String getPlaceOrMarket() { return placeOrMarket; }
    public void setPlaceOrMarket(String placeOrMarket) { this.placeOrMarket = placeOrMarket; }
}

