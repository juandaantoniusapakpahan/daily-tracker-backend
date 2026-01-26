package com.ragdev.tracker.mapper;

import com.ragdev.tracker.dto.ReqFCategoryDto;
import com.ragdev.tracker.dto.ReqFTransactionDto;
import com.ragdev.tracker.dto.ResFTransactionDto;
import com.ragdev.tracker.entity.FinanceTransaction;
import com.ragdev.tracker.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FTransactionMapper {

    public static ResFTransactionDto toDto(FinanceTransaction fTrans) {
        ResFTransactionDto newFTrans = new ResFTransactionDto();
        newFTrans.setId(fTrans.getId());
        newFTrans.setDescription(fTrans.getDescription());
        newFTrans.setTransactionDate(fTrans.getTransactionDate());
        newFTrans.setAmount(fTrans.getAmount());
        newFTrans.setCreatedAt(fTrans.getCreatedAt());
        newFTrans.setUpdatedAt(fTrans.getUpdatedAt());
        newFTrans.setUserId(fTrans.getUser().getId());
        newFTrans.setTransactionType(fTrans.getTransactionType());
        newFTrans.setCategoryId(fTrans.getCategory().getId());
        return newFTrans;
    }

    public static FinanceTransaction toEntity(ReqFTransactionDto dto) {
        FinanceTransaction fTrans = new FinanceTransaction();
        fTrans.setDescription(dto.getDescription());
        fTrans.setTransactionDate(dto.getTransactionDate());
        fTrans.setTransactionType(TransactionType.valueOf(dto.getTransactionType()));
        fTrans.setAmount(dto.getAmount());
        return fTrans;
    }

    public static FinanceTransaction entityToEntity(FinanceTransaction data, ReqFTransactionDto dto) {
        data.setTransactionDate(dto.getTransactionDate());
        data.setTransactionType(TransactionType.valueOf(dto.getTransactionType()));
        data.setAmount(dto.getAmount());
        data.setDescription(dto.getDescription());
        return data;
    }
}
