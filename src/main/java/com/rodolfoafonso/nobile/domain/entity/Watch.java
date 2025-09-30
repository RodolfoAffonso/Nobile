package com.rodolfoafonso.nobile.domain.entity;

import com.rodolfoafonso.nobile.domain.enums.WatchCondition;
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
    @Enumerated(EnumType.STRING)
    private WatchCondition condition;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ElementCollection
    @CollectionTable(name = "watch_images", joinColumns = @JoinColumn(name = "watch_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
