package com.ragdev.tracker.mapper;

import com.ragdev.tracker.dto.ReqFCategoryDto;
import com.ragdev.tracker.dto.ResFCategoryDto;
import com.ragdev.tracker.entity.FinanceCategory;

import java.time.format.DateTimeFormatter;

public class FCategoryMapper {
    public static FinanceCategory toEntity(ReqFCategoryDto dto) {
        FinanceCategory newFCate = new FinanceCategory();
        newFCate.setId(dto.getId());
        newFCate.setName(dto.getName());
        newFCate.setActive(true);
        return newFCate;
    }

    public static ResFCategoryDto toDto(FinanceCategory entity) {
        ResFCategoryDto dto = new ResFCategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setActive(entity.isActive());
        dto.setCreatedAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dto.setUpdatedAt(entity.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return dto;
    }
}
