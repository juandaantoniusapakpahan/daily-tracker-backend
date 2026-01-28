package com.ragdev.tracker.mapper;

import com.ragdev.tracker.dto.ResTodoTaskDto;
import com.ragdev.tracker.entity.TodoTask;

public class TodoTaskMapper {

    public static ResTodoTaskDto toTodoResDto(TodoTask data) {
        ResTodoTaskDto newData = new ResTodoTaskDto();
        newData.setId(data.getId());
        newData.setName(data.getName());
        newData.setDescription(data.getDescription());
        newData.setIsActive(data.getIsActive());
        newData.setCreatedAt(data.getCreatedAt());
        newData.setUpdatedAt(data.getUpdatedAt());
        return newData;
    }
}
