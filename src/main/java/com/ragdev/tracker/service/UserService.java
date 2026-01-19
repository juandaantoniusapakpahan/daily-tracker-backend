package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqRegisterUserDto;
import com.ragdev.tracker.dto.ResUserDto;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.enums.Role;
import com.ragdev.tracker.exception.BadRequestException;
import com.ragdev.tracker.mapper.UserMapper;
import com.ragdev.tracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResUserDto registerUser(ReqRegisterUserDto dto) {
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
        return UserMapper.setUserToDto(newUser);
    }

    public List<User> getActiveUser() {
        return userRepository.findByIsActive(true);
    }

    public List<User> getActiveUserManual() {
        return userRepository.findAllActiveWithTasks();
    }
}
