package com.parampara.bazaar.bargain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parampara.bazaar.product.Product;
import com.parampara.bazaar.user.User;

public interface BargainSessionRepository extends JpaRepository<BargainSession, Long> {

    List<BargainSession> findByBuyerOrderByUpdatedAtDesc(User buyer);

    Optional<BargainSession> findTopByOrderByIdDesc();

    Optional<BargainSession> findTopByBuyerAndProductOrderByUpdatedAtDesc(User buyer, Product product);
}
