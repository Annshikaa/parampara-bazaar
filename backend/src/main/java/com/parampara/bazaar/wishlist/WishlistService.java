package com.parampara.bazaar.wishlist;

import java.util.List; 

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.product.Product;
import com.parampara.bazaar.product.ProductRepository;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    private User currentUser() {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public WishlistItem add(Long productId) {
        User user = currentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("Already in wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setUser(user);
        item.setProduct(product);

        return wishlistRepository.save(item);
    }

    @Transactional
    public void remove(Long productId) {
        User user = currentUser();

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        wishlistRepository.deleteByUserAndProduct(user, product);
}


    public List<WishlistItem> myWishlist() {
        User user = currentUser();
        return wishlistRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public boolean isWishlisted(Long productId) {
        User user = currentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return wishlistRepository.existsByUserAndProduct(user, product);
    }
}
