package com.ragdev.tracker.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Setter @Getter
public class ResAuthDto {
    public String accessToken;
}
