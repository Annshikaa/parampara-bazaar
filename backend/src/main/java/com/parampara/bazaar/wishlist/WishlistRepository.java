package com.parampara.bazaar.wishlist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.parampara.bazaar.product.Product;
import com.parampara.bazaar.user.User;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findByUserOrderByCreatedAtDesc(User user);

    Optional<WishlistItem> findByUserAndProduct(User user, Product product);

    boolean existsByUserAndProduct(User user, Product product);

    @Modifying
    @Transactional
    void deleteByUserAndProduct(User user, Product product);
}

