package com.parampara.bazaar.bargain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.parampara.bazaar.product.Product;
import com.parampara.bazaar.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "bargain_sessions")
public class BargainSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BargainStatus status = BargainStatus.ACTIVE;

    // current AI offer price (starts near originalPrice, moves down but not below minPrice)
    @Column(nullable = false)
    private Double currentOffer;

    // when accepted
    private Double finalPrice;

    @Column(nullable = false)
    private Integer turns = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BargainMessage> messages = new ArrayList<>();

    public BargainSession() {}

    public Long getId() { return id; }

    

    public User getBuyer() { return buyer; }
    public void setBuyer(User buyer) { this.buyer = buyer; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public BargainStatus getStatus() { return status; }
    public void setStatus(BargainStatus status) { this.status = status; }

    public Double getCurrentOffer() { return currentOffer; }
    public void setCurrentOffer(Double currentOffer) { this.currentOffer = currentOffer; }

    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }

    public Integer getTurns() { return turns; }
    public void setTurns(Integer turns) { this.turns = turns; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<BargainMessage> getMessages() { return messages; }
    public void setMessages(List<BargainMessage> messages) { this.messages = messages; }
}
