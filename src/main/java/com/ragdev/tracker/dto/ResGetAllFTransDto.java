package com.ragdev.tracker.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ragdev.tracker.dto.interf.ResTotalFTransDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Setter
@Getter
public class ResGetAllFTransDto {
    private List <ResFTransactionDto> financeTransactions = new ArrayList<>();
    private ResTotalFTransDto total;
}
