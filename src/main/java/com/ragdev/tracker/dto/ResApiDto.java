package com.ragdev.tracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ragdev.tracker.enums.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResApiDto<T, E> {
    private String status;
    private String code;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;
    private T data;
    private E errors;

    public static <T, E> ResApiDto<T, E> success(T data) {
        return new ResApiDto<>("success", ApiCode.SUCCESS_CREATED.getCode(), ApiCode.SUCCESS_CREATED.getMessage(), LocalDateTime.now(), data, null);
    }
}
