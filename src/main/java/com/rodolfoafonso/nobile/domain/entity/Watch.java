package com.rodolfoafonso.nobile.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Watch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private Integer year;
    private String condition;
    private BigDecimal price;

    @ManyToOne
    private User seller;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
