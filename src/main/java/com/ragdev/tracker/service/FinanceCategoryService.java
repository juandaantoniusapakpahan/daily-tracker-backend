package com.ragdev.tracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ragdev.tracker.dto.ReqFCategoryDto;
import com.ragdev.tracker.dto.ResFCategoryDto;
import com.ragdev.tracker.entity.FinanceCategory;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.exception.ResourceNotFoundException;
import com.ragdev.tracker.mapper.FCategoryMapper;
import com.ragdev.tracker.repository.FinanceCategoryRepository;
import com.ragdev.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinanceCategoryService {

    private final FinanceCategoryRepository fCategoryRepo;
    private final RedisService redisService;
    private String getAllKey = "Cate:GetAll";

    public FinanceCategoryService(FinanceCategoryRepository fCategoryRepo,
                                  RedisService redisService) {

        this.fCategoryRepo = fCategoryRepo;
        this.redisService = redisService;
    }

    public ResFCategoryDto create(User user, ReqFCategoryDto dto) {
        String key = getAllKey+user.getId();
        FinanceCategory fCategory = FCategoryMapper.toEntity(dto);
        fCategory.setUser(user);
        fCategoryRepo.save(fCategory);
        redisService.removeDataRedis(key);
        return FCategoryMapper.toDto(fCategory);
    }

    @Transactional
    public ResFCategoryDto update(Long userId, ReqFCategoryDto dto) {
        String key = getAllKey+userId;
        FinanceCategory fCategory = getById(dto.getId());
        fCategory.setName(dto.getName());
        fCategoryRepo.save(fCategory);
        redisService.removeDataRedis(key);
        return FCategoryMapper.toDto(fCategory);
    }

    @Transactional
    public ResFCategoryDto inActive(Long userId,Long id) {
        String key = getAllKey+userId;
        FinanceCategory fCategory = getById(id);
        fCategory.setActive(false);
        fCategoryRepo.save(fCategory);
        redisService.removeDataRedis(key);
        return FCategoryMapper.toDto(fCategory);
    }

    @Transactional
    public ResFCategoryDto active(Long userId, Long id) {
        String key = getAllKey+userId;
        FinanceCategory fCategory = getById(id);
        fCategory.setActive(true);
        fCategoryRepo.save(fCategory);
        redisService.removeDataRedis(key);
        return FCategoryMapper.toDto(fCategory);
    }

    public FinanceCategory getById(Long id) {
       return fCategoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found"));
    }

    public List<ResFCategoryDto> getAll(Long userId)  {
        String key = getAllKey + userId;
        List<ResFCategoryDto> redisData = redisService.getValueList(key, ResFCategoryDto.class);

        if(redisData == null){
            redisData = fCategoryRepo.findByUserId(userId)
                    .stream()
                    .map(FCategoryMapper::toDto)
                    .toList();
            redisService.saveToRedis(key, redisData);
        }
        return redisData;
    }
}
