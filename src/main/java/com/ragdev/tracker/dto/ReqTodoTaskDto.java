package com.ragdev.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqTodoTaskDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Description can not null value")
    private String description;
}
