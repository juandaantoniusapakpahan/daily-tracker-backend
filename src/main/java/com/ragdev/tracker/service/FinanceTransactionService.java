package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqFTransactionDto;
import com.ragdev.tracker.dto.ResFTransactionDto;
import com.ragdev.tracker.entity.FinanceCategory;
import com.ragdev.tracker.entity.FinanceTransaction;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.enums.TransactionType;
import com.ragdev.tracker.exception.ResourceNotFoundException;
import com.ragdev.tracker.mapper.FTransactionMapper;
import com.ragdev.tracker.repository.FinanceCategoryRepository;
import com.ragdev.tracker.repository.FinanceTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FinanceTransactionService {

    private final FinanceTransactionRepository fTransRepo;
    private final FinanceCategoryRepository fCateRepo;

    public FinanceTransactionService(FinanceTransactionRepository fTransRepo,
                                     FinanceCategoryRepository fCateRepo) {
        this.fTransRepo = fTransRepo;
        this.fCateRepo = fCateRepo;
    }

    @Transactional
    public ResFTransactionDto create(User user, ReqFTransactionDto dto) {
        FinanceCategory fCate = getFCateById(dto.getCategoryId());
        FinanceTransaction fTrans = FTransactionMapper.toEntity(dto);
        fTrans.setUser(user);
        fTrans.setCategory(fCate);
        fTransRepo.save(fTrans);
        return FTransactionMapper.toDto(fTrans);
    }

    @Transactional
    public ResFTransactionDto update(Long id, ReqFTransactionDto dto) {
        FinanceCategory fCate = getFCateById(dto.getCategoryId());
        FinanceTransaction fTrans = FTransactionMapper.entityToEntity(getById(id),dto);
        fTrans.setCategory(fCate);
        fTransRepo.save(fTrans);
        return FTransactionMapper.toDto(fTrans);
    }


    public FinanceCategory getFCateById(Long id) {
        return fCateRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Finance Category not found"));
    }

    public FinanceTransaction getById(Long id) {
        return fTransRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }
}
