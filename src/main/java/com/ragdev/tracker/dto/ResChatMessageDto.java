package com.ragdev.tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ResChatMessageDto {
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime timestamp;
    private boolean mine;

}
