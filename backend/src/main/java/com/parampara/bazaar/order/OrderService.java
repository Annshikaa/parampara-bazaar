package com.parampara.bazaar.order;

import com.parampara.bazaar.cart.CartItem;
import com.parampara.bazaar.cart.CartRepository;
import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.common.enums.OrderStatus;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.parampara.bazaar.bargain.BargainSession;
import com.parampara.bazaar.bargain.BargainSessionRepository;
import com.parampara.bazaar.bargain.BargainStatus;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BargainSessionRepository bargainRepo;

    public OrderService(OrderRepository orderRepository,
        CartRepository cartRepository,
        UserRepository userRepository,
        BargainSessionRepository bargainRepo) {
            this.orderRepository = orderRepository;
            this.cartRepository = cartRepository;
            this.userRepository = userRepository;
            this.bargainRepo = bargainRepo;
}

    private User currentUser() {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public Order checkout() {
        User user = currentUser();

        List<CartItem> cartItems = cartRepository.findByUserOrderByUpdatedAtDesc(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalOriginal = 0;
        double totalMin = 0;

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        for (CartItem c : cartItems) {
            double qty = c.getQuantity();
            
            double unitPrice = c.getProduct().getMinPrice();

// If bargained
            if (c.getBargainSessionId() != null) {
               BargainSession s = bargainRepo.findById(c.getBargainSessionId()).orElse(null);

               if (s != null && s.getStatus() == BargainStatus.ACCEPTED) {
               unitPrice = s.getFinalPrice();
    }
}

            totalOriginal += c.getProduct().getOriginalPrice() * qty;
            totalMin += unitPrice * qty;

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProductId(c.getProduct().getId());
            oi.setProductName(c.getProduct().getName());
            oi.setOriginalPrice(c.getProduct().getOriginalPrice());
            oi.setMinPrice(c.getProduct().getMinPrice());
            oi.setFinalPrice(unitPrice);
            oi.setQuantity(c.getQuantity());
            oi.setVendorId(c.getProduct().getVendor().getId());


            order.getItems().add(oi);
        }

        order.setTotalOriginalPrice(totalOriginal);
        order.setTotalMinPrice(totalMin);

        Order saved = orderRepository.save(order);

        // clear cart after order created
        cartRepository.deleteByUser(user);

        return saved;
    }

    public List<Order> myOrders() {
        User user = currentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Order getMyOrderById(Long id) {
        User user = currentUser();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        return order;
    }
}

