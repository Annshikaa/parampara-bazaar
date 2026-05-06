package com.parampara.bazaar.product;

import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;
import com.parampara.bazaar.vendor.VendorProfile;
import com.parampara.bazaar.vendor.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
    }

    public Product addProduct(ProductCreateRequest req) {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VendorProfile vendor = vendorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Create vendor profile first"));

        if (req.getMinPrice() > req.getOriginalPrice()) {
            throw new RuntimeException("minPrice cannot be greater than originalPrice");
        }

        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setOriginalPrice(req.getOriginalPrice());
        p.setMinPrice(req.getMinPrice());
        p.setCategory(req.getCategory());
        p.setImageUrl(req.getImageUrl());
        p.setVendor(vendor);
        p.setVendorType(req.getVendorType());
        p.setPlaceOrMarket(req.getPlaceOrMarket());

        return productRepository.save(p);
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    }