package com.ragdev.tracker.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReqGetTotalDto {
    private LocalDate start;
    private LocalDate end;
}
