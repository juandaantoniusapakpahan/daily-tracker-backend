package com.ragdev.tracker.mapper;

import com.ragdev.tracker.dto.ResTodoCheckListDto;
import com.ragdev.tracker.entity.TodoCheckList;

public class TodoCheckListMapper {
    public static ResTodoCheckListDto toDto(TodoCheckList checkList) {
        ResTodoCheckListDto newCheck = new ResTodoCheckListDto();
        newCheck.setCheckListId(checkList.getId());
        newCheck.setCheckDate(checkList.getCheckDate());
        newCheck.setIsChecked(checkList.getIsChecked());
        newCheck.setTaskId(checkList.getTask().getId());
        newCheck.setUserId(checkList.getUser().getId());
        return newCheck;
    }
}
