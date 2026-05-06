package com.parampara.bazaar.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parampara.bazaar.product.Product;
import com.parampara.bazaar.user.User;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserOrderByUpdatedAtDesc(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    boolean existsByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}
