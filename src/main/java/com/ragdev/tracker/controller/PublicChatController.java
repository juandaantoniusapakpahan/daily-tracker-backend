package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ReqChatMessageDto;
import com.ragdev.tracker.dto.ResChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PublicChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload ReqChatMessageDto request,
            java.security.Principal principal) { // Gunakan Principal, bukan UserDetailsImpl langsung

        if (principal == null) {
            throw new RuntimeException("User Unauthorized");
        }

        // Ambil username dari principal
        String username = principal.getName();

        ResChatMessageDto res = new ResChatMessageDto();
        res.setUsername(username);
        res.setContent(request.getContent());
        res.setTimestamp(java.time.LocalDateTime.now());

        // Note: 'mine' akan kita tentukan di Frontend agar lebih akurat
        // karena broadcast dikirim ke semua orang termasuk pengirim.

        messagingTemplate.convertAndSend("/topic/public", res);
    }
}
