package com.ragdev.tracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ragdev.tracker.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class ResUserDto {
    private Long id;
    private String email;
    private String username;
    private Role role;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
