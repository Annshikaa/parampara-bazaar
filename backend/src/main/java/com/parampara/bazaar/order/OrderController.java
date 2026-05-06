package com.parampara.bazaar.order;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/checkout")
    public Order checkout() {
        return orderService.checkout();
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping
    public List<Order> myOrders() {
        return orderService.myOrders();
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/{id}")
    public Order myOrder(@PathVariable Long id) {
        return orderService.getMyOrderById(id);
    }
}

