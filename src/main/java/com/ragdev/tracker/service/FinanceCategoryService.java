package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqFCategoryDto;
import com.ragdev.tracker.dto.ResFCategoryDto;
import com.ragdev.tracker.entity.FinanceCategory;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.exception.ResourceNotFoundException;
import com.ragdev.tracker.mapper.FCategoryMapper;
import com.ragdev.tracker.repository.FinanceCategoryRepository;
import com.ragdev.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceCategoryService {

    private final FinanceCategoryRepository fCategoryRepo;
    private final UserRepository userRepository;
    public FinanceCategoryService(FinanceCategoryRepository fCategoryRepo,
                                  UserRepository userRepository) {
        this.fCategoryRepo = fCategoryRepo;
        this.userRepository = userRepository;
    }

    public ResFCategoryDto create(User user, ReqFCategoryDto dto) {
        FinanceCategory fCategory = FCategoryMapper.toEntity(dto);
        fCategory.setUser(user);
        fCategoryRepo.save(fCategory);
        return FCategoryMapper.toDto(fCategory);
    }

    @Transactional
    public ResFCategoryDto update(ReqFCategoryDto dto) {
        FinanceCategory fCategory = getById(dto.getId());
        fCategory.setName(dto.getName());
        fCategoryRepo.save(fCategory);
        return FCategoryMapper.toDto(fCategory);
    }

    @Transactional
    public ResFCategoryDto inActive(Long id) {
        FinanceCategory fCategory = getById(id);
        fCategory.setActive(false);
        fCategoryRepo.save(fCategory);
        return FCategoryMapper.toDto(fCategory);
    }

    @Transactional
    public ResFCategoryDto active(Long id) {
        FinanceCategory fCategory = getById(id);
        fCategory.setActive(true);
        fCategoryRepo.save(fCategory);
        return FCategoryMapper.toDto(fCategory);
    }

    public FinanceCategory getById(Long id) {
       return fCategoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found"));
    }

    public List<ResFCategoryDto> getAll(Long userId) {
        return fCategoryRepo.findByUserId(userId).stream().map(FCategoryMapper::toDto).toList();
    }

}
