package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqFTransactionDto;
import com.ragdev.tracker.dto.ReqGetAllFTransDto;
import com.ragdev.tracker.dto.ResFTransactionDto;
import com.ragdev.tracker.dto.ResGetAllFTransDto;
import com.ragdev.tracker.dto.interf.ResMonthlyTotalDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.support.RestGatewaySupport;

import java.time.LocalDate;
import java.util.*;

@Service
public class FinanceTransactionService {

    private final FinanceTransactionRepository fTransRepo;
    private final FinanceCategoryRepository fCateRepo;

    public FinanceTransactionService(FinanceTransactionRepository fTransRepo,
                                     FinanceCategoryRepository fCateRepo
                                 ) {
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
    public ResFTransactionDto update(Long userId,Long id, ReqFTransactionDto dto) {
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

    public List<FinanceTransaction> findFinsTrans(Long userId, LocalDate start,
                                                 LocalDate end) {
        return fTransRepo.findByUserIdAndTransactionDate(userId, start, end);
    }
    public List<FinanceTransaction> getFinsTrans(Long userId, LocalDate start,
                                                 LocalDate end,
                                                 Pageable pageable) {
        return fTransRepo.getByUserIdAndTransactionDate(userId, start, end,pageable);
    }

    public List<FinanceTransaction> getByUserIdAndTransactionDateAndType(Long userId, LocalDate start,
                                                                         LocalDate end, String type,
                                                                         Pageable pageable) {
        return fTransRepo.getByUserIdAndTransactionDateAndType(userId, type, start, end, pageable);
    }

    public ResTotalFTransDto getTotal(Long userId, LocalDate start, LocalDate end) {
        return fTransRepo.getTotalIncomeExpense(userId, start, end);
    }

    @Transactional
    public void deleteById(Long userId,Long id) {
        fTransRepo.deleteById(id);
    }

    public List<ResFTransactionDto> getAll(Long id) {
        return fTransRepo.findByUserId(id).stream().map(FTransactionMapper::toDto).toList();
    }

    public ResGetAllFTransDto getFTransCurrMonth(Long userId) {
        ResGetAllFTransDto redisData = new ResGetAllFTransDto();
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();
        List<ResFTransactionDto> fTransDto = findFinsTrans(userId, start, end).stream()
                .map(FTransactionMapper::toDto).toList();

        redisData.setFinanceTransactions(fTransDto);
        redisData.setTotal(getTotal(userId, start, end));
        return redisData;

    }
    public ResGetAllFTransDto getAllByTransactionWithType(Long userId, ReqGetAllFTransDto dto,
                                                          int page, int size) {
        Pageable pageable  = PageRequest.of(page, size);
        List<FinanceTransaction> fTrans= new ArrayList<>();
        int totalData = 0;
        if ((dto.isExpense() && dto.isIncome()) || (!dto.isExpense() && !dto.isIncome())) {
            fTrans = getFinsTrans(userId, dto.getStart(), dto.getEnd(),pageable);
            totalData = getTotalDataTrans( userId, dto.getStart(), dto.getEnd(), "");
        } else {
            String type =  dto.isExpense() ? "EXPENSE" : "INCOME";
            fTrans = getByUserIdAndTransactionDateAndType(userId, dto.getStart(), dto.getEnd(), type,pageable);
            totalData = getTotalDataTrans(userId, dto.getStart(), dto.getEnd(), type);
        }

        ResGetAllFTransDto getAllDto = new ResGetAllFTransDto();
        getAllDto.setFinanceTransactions(fTrans.stream().map(FTransactionMapper::toDto).toList());
        getAllDto.setTotal(getTotal(userId, dto.getStart(), dto.getEnd()));
        getAllDto.setTotalData(totalData);
        getAllDto.setTotalPage((int) (double) (totalData / pageable.getPageSize()));
        return getAllDto;
    }

    public Map<String, List<ResMonthlyTotalDto>> findMonthlyIncomeExpense(Long userId, int year) {
        Map<String, List<ResMonthlyTotalDto>> dto = new HashMap<>();
        dto.put("transactionMonthly", fTransRepo.findMonthlyIncomeExpense(userId, year));
        return dto;
    }

    public Integer getTotalDataTrans( Long userId, LocalDate start, LocalDate end, String type) {
        if (!type.isEmpty()) {
            return fTransRepo.getTotalDataByType(userId, type, start, end);
        } else {
            return fTransRepo.getTotalData(userId, start, end);
        }
    }


}
