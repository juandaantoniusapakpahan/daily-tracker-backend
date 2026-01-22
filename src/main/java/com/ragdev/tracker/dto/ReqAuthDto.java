package com.ragdev.tracker.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReqAuthDto {
    private String username;
    private String password;
}
