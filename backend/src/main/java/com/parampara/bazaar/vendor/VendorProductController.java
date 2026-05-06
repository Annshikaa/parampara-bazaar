package com.parampara.bazaar.vendor;

import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.common.dto.ApiResponse;
import com.parampara.bazaar.product.Product;
import com.parampara.bazaar.product.ProductRepository;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/vendor/products")
public class VendorProductController {

    private final ProductRepository productRepository;
    private final VendorProfileRepository vendorProfileRepository;
    private final UserRepository userRepository;

    public VendorProductController(ProductRepository productRepository,
                                   VendorProfileRepository vendorProfileRepository,
                                   UserRepository userRepository) {
        this.productRepository = productRepository;
        this.vendorProfileRepository = vendorProfileRepository;
        this.userRepository = userRepository;
    }

    private User currentUser() {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private VendorProfile currentVendorProfile() {
        User user = currentUser();
        return vendorProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Vendor profile not found"));
    }

    // ✅ Get vendor’s own products
    @GetMapping
    public ResponseEntity<?> myProducts() {
        VendorProfile vendor = currentVendorProfile();
        return ResponseEntity.ok(productRepository.findByVendorId(vendor.getId()));
    }

    // ✅ Create product
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product req) {
        VendorProfile vendor = currentVendorProfile();

        if (req.getName() == null || req.getName().isBlank())
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Name required"));
        if (req.getCategory() == null || req.getCategory().isBlank())
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Category required"));
        if (req.getPlaceOrMarket() == null || req.getPlaceOrMarket().isBlank())
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Place/Market required"));
        if (req.getOriginalPrice() == null || req.getOriginalPrice() <= 0)
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Original price must be > 0"));
        if (req.getMinPrice() == null || req.getMinPrice() <= 0)
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Min price must be > 0"));
        if (req.getMinPrice() > req.getOriginalPrice())
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Min price cannot be greater than original price"));

        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setImageUrl(req.getImageUrl());
        p.setCategory(req.getCategory());
        p.setPlaceOrMarket(req.getPlaceOrMarket());

        p.setOriginalPrice(req.getOriginalPrice());
        p.setMinPrice(req.getMinPrice());

        // Vendor info is derived from profile (never trust client for this)
        p.setVendor(vendor);
        p.setVendorType(vendor.getVendorType());

        productRepository.save(p);
        return ResponseEntity.ok(p);
    }

    // ✅ Update product (only if it belongs to vendor)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        VendorProfile vendor = currentVendorProfile();

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!p.getVendor().getId().equals(vendor.getId()))
            return ResponseEntity.status(403).body(new ApiResponse(false, "Forbidden"));

        if (body.containsKey("name")) p.setName((String) body.get("name"));
        if (body.containsKey("description")) p.setDescription((String) body.get("description"));
        if (body.containsKey("imageUrl")) p.setImageUrl((String) body.get("imageUrl"));
        if (body.containsKey("category")) p.setCategory((String) body.get("category"));
        if (body.containsKey("placeOrMarket")) p.setPlaceOrMarket((String) body.get("placeOrMarket"));

        if (body.containsKey("originalPrice")) {
            Double op = Double.valueOf(body.get("originalPrice").toString());
            if (op <= 0) return ResponseEntity.badRequest().body(new ApiResponse(false, "Original price must be > 0"));
            p.setOriginalPrice(op);
        }

        if (body.containsKey("minPrice")) {
            Double mp = Double.valueOf(body.get("minPrice").toString());
            if (mp <= 0) return ResponseEntity.badRequest().body(new ApiResponse(false, "Min price must be > 0"));
            p.setMinPrice(mp);
        }

        if (p.getMinPrice() != null && p.getOriginalPrice() != null && p.getMinPrice() > p.getOriginalPrice()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Min price cannot be greater than original price"));
        }

        productRepository.save(p);
        return ResponseEntity.ok(p);
    }

    // ✅ Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        VendorProfile vendor = currentVendorProfile();

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!p.getVendor().getId().equals(vendor.getId()))
            return ResponseEntity.status(403).body(new ApiResponse(false, "Forbidden"));

        productRepository.delete(p);
        return ResponseEntity.ok(new ApiResponse(true, "Deleted"));
    }
}

