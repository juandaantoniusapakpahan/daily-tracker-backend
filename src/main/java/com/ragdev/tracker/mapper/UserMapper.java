package com.ragdev.tracker.mapper;

import com.ragdev.tracker.dto.ResUserDto;
import com.ragdev.tracker.entity.User;

public class UserMapper {

    public static ResUserDto setUserToDto(User user) {
        ResUserDto newDto = new ResUserDto();
        newDto.setId(user.getId());
        newDto.setRole(user.getRole());
        newDto.setEmail(user.getEmail());
        newDto.setIsActive(user.getIsActive());
        return newDto;
    }
}
