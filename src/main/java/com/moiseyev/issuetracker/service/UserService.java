package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.EmailAlreadyExistsException;
import com.moiseyev.issuetracker.exception.LoginAlreadyExistsException;
import com.moiseyev.issuetracker.exception.UserNotFoundException;
import com.moiseyev.issuetracker.exception.UserUpdateException;
import com.moiseyev.issuetracker.model.dto.UserCreateDto;
import com.moiseyev.issuetracker.model.dto.UserResponseDto;
import com.moiseyev.issuetracker.model.dto.UserUpdateDto;
import com.moiseyev.issuetracker.model.entity.Role;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.model.enums.RoleType;
import com.moiseyev.issuetracker.model.mapper.UserMapper;
import com.moiseyev.issuetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class UserService {
  private final UserRepository userRepository;
  private final RoleService roleService;
  private final UserMapper userMapper;

  @Autowired
  public UserService(UserRepository userRepository, RoleService roleService, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.roleService = roleService;
    this.userMapper = userMapper;
  }

  public List<UserResponseDto> getAllUsers() {
    return userRepository
            .findAll()
            .stream()
            .map(userMapper::toResponseDto)
            .toList();
  }

  public UserResponseDto getUserById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    return userMapper.toResponseDto(user);
  }

  @Transactional
  public UserResponseDto save(UserCreateDto dto) {
    validateUniqueFields(null, dto.getLogin(), dto.getEmail());

    if (dto.getRoleType() == null) {
      dto.setRoleType(RoleType.REPORTER.toString());
    }
    Role role = roleService.findRoleByName(dto.getRoleType());

    User user = new User();
    user.setLogin(dto.getLogin());
    user.setEmail(dto.getEmail());
    user.setPassword(dto.getPassword());
    user.setCreatedAt(Instant.now());
    user.setUpdatedAt(Instant.now());
    user.setRole(role);

    log.info("Creating user with login = {}", dto.getLogin());
    return userMapper.toResponseDto(userRepository.save(user));
  }

  @Transactional
  public UserResponseDto update(UserUpdateDto dto) {
    User user = userRepository.findById(dto.getId())
            .orElseThrow(() -> new UserNotFoundException(dto.getId()));
    boolean hasChanges = false;

    if (!user.getLogin().equals(dto.getLogin())) {
      validateLoginUniqueness(dto.getId(), dto.getLogin());
      user.setLogin(dto.getLogin());
      hasChanges = true;
      log.info("Login changed to: {}", dto.getLogin());
    }

    if (!user.getEmail().equals(dto.getEmail())) {
      validateEmailUniqueness(dto.getId(), dto.getEmail());
      user.setEmail(dto.getEmail());
      hasChanges = true;
      log.info("Email changed to: {}", dto.getEmail());
    }

    if (!user.getPassword().equals(dto.getPassword())) {
      user.setPassword(dto.getPassword());
      hasChanges = true;
      log.info("Password changed");
    }

    if (!hasChanges) {
      throw new UserUpdateException(dto.getId());
    }

    user.setUpdatedAt(Instant.now());
    log.info("Updating user with id = {}", dto.getId());
    User updatedUser = userRepository.saveAndFlush(user);
    return userMapper.toResponseDto(updatedUser);
  }

  @Transactional
  public void delete(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    log.info("Deleting user with id = {}", id);
    userRepository.delete(user);
  }

  private void validateUniqueFields(Long excludeId, String login, String email) {
    validateLoginUniqueness(excludeId, login);
    validateEmailUniqueness(excludeId, email);
  }

  private void validateEmailUniqueness(Long excludeId, String email) {
    if (emailIsTaken(excludeId, email)) {
      throw new EmailAlreadyExistsException(email);
    }
  }

  private void validateLoginUniqueness(Long excludeId, String login) {
    if (loginIsTaken(excludeId, login)) {
      throw new LoginAlreadyExistsException(login);
    }
  }

  private boolean loginIsTaken(Long excludeId, String login) {
    return userRepository.findUserByLogin(login)
            .map(u -> !u.getId().equals(excludeId))
            .orElse(false);
  }

  private boolean emailIsTaken(Long excludeId, String email) {
    return userRepository.findUserByEmail(email)
            .map(u -> !u.getId().equals(excludeId))
            .orElse(false);
  }
}
