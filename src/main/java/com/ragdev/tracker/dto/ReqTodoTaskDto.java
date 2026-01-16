package com.ragdev.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqTodoTaskDto {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
