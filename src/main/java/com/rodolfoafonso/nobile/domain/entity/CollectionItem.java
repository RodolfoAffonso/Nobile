package com.rodolfoafonso.nobile.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CollectionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Watch watch;

    private BigDecimal estimatedValue;
}
