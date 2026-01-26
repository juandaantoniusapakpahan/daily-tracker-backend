package com.ragdev.tracker.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResHabitTrackerDto {
    private String month;
    private String today;
    private List<String> dates;

    private List<ResTodoTaskDto> tasks;
}
