package com.rodolfoafonso.nobile.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User buyer;

    @OneToMany
    private List<Watch> items;

    private String status;

    private String paymentIntentId; // Para integração com Stripe

    private LocalDateTime createdAt = LocalDateTime.now();
}
