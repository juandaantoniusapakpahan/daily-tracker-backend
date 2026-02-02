package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ReqChatMessageDto;
import com.ragdev.tracker.dto.ResChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PublicChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload ReqChatMessageDto request,
            Principal principal) {

        if (principal == null) {
            throw new RuntimeException("User Unauthorized");
        }

        String username = principal.getName();

        ResChatMessageDto res = new ResChatMessageDto();
        res.setUsername(username);
        res.setContent(request.getContent());
        res.setTimestamp(java.time.LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/public", res);
    }
}
