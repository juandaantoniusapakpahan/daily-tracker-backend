package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqAuthDto;
import com.ragdev.tracker.dto.ReqRegisterUserDto;
import com.ragdev.tracker.dto.ResAuthDto;
import com.ragdev.tracker.dto.ResUserDto;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.enums.Role;
import com.ragdev.tracker.exception.BadRequestException;
import com.ragdev.tracker.mapper.UserMapper;
import com.ragdev.tracker.repository.UserRepository;
import com.ragdev.tracker.security.JwtService;
import io.jsonwebtoken.Jwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResAuthDto registerUser(ReqRegisterUserDto dto) {
        if (userRepository.existsByUsernameOrEmail(dto.getUsername(), dto.getEmail())) {
            throw new BadRequestException("Username or email already exists");
        }

        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setRole(Role.USER);
        newUser.setIsActive(true);
        userRepository.save(newUser);

        ResAuthDto resDto = new ResAuthDto();
        resDto.setAccessToken(jwtService.generateToken(newUser.getUsername()));
        return resDto;
    }

    public List<User> getActiveUser() {
        return userRepository.findByIsActive(true);
    }

    public List<User> getActiveUserManual() {
        return userRepository.findAllActiveWithTasks();
    }

    public ResAuthDto login(ReqAuthDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        ResAuthDto resAuthDto = new ResAuthDto();
        resAuthDto.setAccessToken(jwtService.generateToken(dto.getUsername()));
        return resAuthDto;
    }
}
