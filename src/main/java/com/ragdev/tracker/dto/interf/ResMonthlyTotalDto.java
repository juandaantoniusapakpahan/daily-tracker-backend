package com.ragdev.tracker.dto.interf;

import java.math.BigDecimal;

public interface ResMonthlyTotalDto {
    String getMonthName();
    String getType();
    BigDecimal getTotalAmount();
}
