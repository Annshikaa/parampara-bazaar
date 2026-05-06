package com.parampara.bazaar.cart;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.common.dto.AddToCartRequest;
import com.parampara.bazaar.common.dto.UpdateCartQtyRequest;
import com.parampara.bazaar.product.Product;
import com.parampara.bazaar.product.ProductRepository;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    private User currentUser() {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public CartItem add(AddToCartRequest req) {
        User user = currentUser();
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartRepository.findByUserAndProduct(user, product).orElse(new CartItem());
        item.setUser(user);
        item.setProduct(product);

        int newQty = (item.getQuantity() == null ? 0 : item.getQuantity()) + req.getQuantity();
        item.setQuantity(newQty);
        item.setBargainSessionId(req.getBargainSessionId());
        item.setUpdatedAt(LocalDateTime.now());

        return cartRepository.save(item);
    }

    public CartItem updateQty(UpdateCartQtyRequest req) {
        User user = currentUser();
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        item.setQuantity(req.getQuantity());
        item.setUpdatedAt(LocalDateTime.now());

        return cartRepository.save(item);
    }
    
    @Transactional
    public void remove(Long productId) {
        User user = currentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartRepository.deleteByUserAndProduct(user, product);
    }

    public CartResponse myCart() {
        User user = currentUser();
        List<CartItem> items = cartRepository.findByUserOrderByUpdatedAtDesc(user);

        double totalOriginal = 0;
        double totalMin = 0;

        for (CartItem i : items) {
            double qty = i.getQuantity();
            totalOriginal += i.getProduct().getOriginalPrice() * qty;
            totalMin += i.getProduct().getMinPrice() * qty;
        }

        return new CartResponse(items, totalOriginal, totalMin);
    }
    
    @Transactional
    public void clear() {
        User user = currentUser();
        cartRepository.deleteByUser(user);
    }
}
