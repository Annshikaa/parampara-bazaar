package com.parampara.bazaar.bargain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bargain_messages")
public class BargainMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private BargainSession session;

    @Column(nullable = false)
    private String sender; // BUYER or BOT

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    // optional numeric offer proposed by buyer or bot in that message
    private Double offer;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public BargainMessage() {}

    public Long getId() { return id; }

    public BargainSession getSession() { return session; }
    public void setSession(BargainSession session) { this.session = session; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Double getOffer() { return offer; }
    public void setOffer(Double offer) { this.offer = offer; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
