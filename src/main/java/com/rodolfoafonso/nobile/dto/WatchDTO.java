package com.rodolfoafonso.nobile.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchDTO {
    private String brand;
    private String model;
    private Integer year;
    private String condition;
    private BigDecimal price;
    private List<String> images;
}
