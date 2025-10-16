package com.autotax.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxDto {
    private Long id;
    private String taxpayerId;
    private String taxType;
    private BigDecimal amount;
    private LocalDate taxDate;
    private String status;
}
