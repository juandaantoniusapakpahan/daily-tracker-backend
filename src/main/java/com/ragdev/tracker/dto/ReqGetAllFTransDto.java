package com.ragdev.tracker.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReqGetAllFTransDto {
    private boolean income;
    private boolean expense;
    private LocalDate start;
    private LocalDate end;
}
