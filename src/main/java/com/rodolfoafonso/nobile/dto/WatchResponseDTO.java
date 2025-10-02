package com.rodolfoafonso.nobile.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchResponseDTO {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String condition;
    private BigDecimal price;

    // Informações do vendedor
    private Long sellerId;
    private String sellerName;

    private List<String> images;
}
