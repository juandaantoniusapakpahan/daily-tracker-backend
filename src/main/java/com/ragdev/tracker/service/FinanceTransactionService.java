package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqFTransactionDto;
import com.ragdev.tracker.dto.ReqGetAllFTransDto;
import com.ragdev.tracker.dto.ResFTransactionDto;
import com.ragdev.tracker.dto.ResGetAllFTransDto;
import com.ragdev.tracker.dto.interf.ResTotalFTransDto;
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

import java.time.LocalDate;
import java.util.*;

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

    public Map<String, List<TransactionType>> getType() {
        Map<String, List<TransactionType>> map = new HashMap<>();
        map.put("types", Arrays.asList(TransactionType.values()));
        return map;
    }

    public List<FinanceTransaction> getFinsTrans(Long userId, LocalDate start, LocalDate end) {
        return fTransRepo.getByUserIdAndTransactionDate(userId, start, end);
    }

    public List<FinanceTransaction> getByUserIdAndTransactionDateAndType(Long userId, LocalDate start, LocalDate end, String type) {
        return fTransRepo.getByUserIdAndTransactionDateAndType(userId, type, start, end);
    }

    public ResTotalFTransDto getTotal(Long userId, LocalDate start, LocalDate end) {
        return fTransRepo.getTotalIncomeExpense(userId, start, end);
    }

    @Transactional
    public void deleteById(Long id) {
        fTransRepo.deleteById(id);
    }

    public List<ResFTransactionDto> getAll(Long id) {
        return fTransRepo.findByUserId(id).stream().map(FTransactionMapper::toDto).toList();
    }

    public ResGetAllFTransDto getFTransCurrMonth(Long userId) {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();
        List<ResFTransactionDto> fTransDto = getFinsTrans(userId, start, end).stream()
                .map(FTransactionMapper::toDto).toList();

        ResGetAllFTransDto allDto =  new ResGetAllFTransDto();
        allDto.setFinanceTransactions(fTransDto);
        allDto.setTotal(getTotal(userId, start, end));
        return allDto;

    }
    public ResGetAllFTransDto getAllByTransactionWithType(Long userId, ReqGetAllFTransDto dto) {
        List<FinanceTransaction> fTrans= new ArrayList<>();
        if ((dto.isExpense() && dto.isIncome()) || (!dto.isExpense() && !dto.isIncome())) {
            fTrans = getFinsTrans(userId, dto.getStart(), dto.getEnd());
        } else if (!dto.isExpense()) {
            fTrans = getByUserIdAndTransactionDateAndType(userId, dto.getStart(), dto.getEnd(), "INCOME");
        } else {
            fTrans = getByUserIdAndTransactionDateAndType(userId, dto.getStart(), dto.getEnd(), "EXPENSE");
        }
        ResGetAllFTransDto getAllDto = new ResGetAllFTransDto();
        getAllDto.setFinanceTransactions(fTrans.stream().map(FTransactionMapper::toDto).toList());
        getAllDto.setTotal(getTotal(userId, dto.getStart(), dto.getEnd()));
        return getAllDto;
    }


}
