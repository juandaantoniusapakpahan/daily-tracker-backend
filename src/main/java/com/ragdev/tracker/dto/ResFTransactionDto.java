package com.ragdev.tracker.dto;

import com.ragdev.tracker.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class ResFTransactionDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String description;
    private LocalDate transactionDate;
    private TransactionType transactionType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
