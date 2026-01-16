package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.dto.ReqRegisterUserDto;
import com.ragdev.tracker.dto.ResUserDto;
import com.ragdev.tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResApiDto<Object,Object>> registerUser(@Valid @RequestBody ReqRegisterUserDto dto) {
        ResUserDto resDto = userService.registerUser(dto);
        return ResponseEntity.ok(ResApiDto.created(resDto));
    }
}
