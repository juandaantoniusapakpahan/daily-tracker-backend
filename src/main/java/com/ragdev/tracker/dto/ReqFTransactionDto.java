package com.ragdev.tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class ReqFTransactionDto {
    private Long id;
    private Long categoryId;
    private String description;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private String transactionType;
}
