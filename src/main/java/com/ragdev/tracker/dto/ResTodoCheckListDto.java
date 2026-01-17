package com.ragdev.tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ResTodoCheckListDto {
    private Long checkListId;
    private Boolean isChecked;
    private LocalDate checkDate;
    private Long taskId;
    private Long userId;
}
