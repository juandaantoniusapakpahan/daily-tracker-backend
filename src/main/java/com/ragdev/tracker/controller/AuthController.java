package com.ragdev.tracker.controller;


import com.ragdev.tracker.dto.ReqAuthDto;
import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.dto.ResAuthDto;
import com.ragdev.tracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResApiDto<Object, Object>> login(@RequestBody ReqAuthDto dto) {
        return ResponseEntity.ok(ResApiDto.ok(userService.login(dto)));
    }
}
