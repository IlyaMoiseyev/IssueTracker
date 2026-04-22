package com.moiseyev.issuetracker.model.mapper;

import com.moiseyev.issuetracker.model.dto.UserResponseDto;
import com.moiseyev.issuetracker.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserResponseDto toResponseDto(User user) {
    return UserResponseDto.builder()
            .id(user.getId())
            .login(user.getLogin())
            .email(user.getEmail())
            .role(user.getRole().getName().toString())
            .build();
  }
}
