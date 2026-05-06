package com.parampara.bazaar.order;

import com.parampara.bazaar.common.enums.OrderStatus;
import com.parampara.bazaar.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(nullable = false)
    private Double totalOriginalPrice;

    @Column(nullable = false)
    private Double totalMinPrice;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Double getTotalOriginalPrice() { return totalOriginalPrice; }
    public void setTotalOriginalPrice(Double totalOriginalPrice) { this.totalOriginalPrice = totalOriginalPrice; }

    public Double getTotalMinPrice() { return totalMinPrice; }
    public void setTotalMinPrice(Double totalMinPrice) { this.totalMinPrice = totalMinPrice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
