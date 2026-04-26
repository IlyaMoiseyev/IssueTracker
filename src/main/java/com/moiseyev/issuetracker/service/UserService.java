package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.EmailAlreadyExistsException;
import com.moiseyev.issuetracker.exception.LoginAlreadyExistsException;
import com.moiseyev.issuetracker.exception.RegistrationException;
import com.moiseyev.issuetracker.exception.UserNotFoundException;
import com.moiseyev.issuetracker.exception.UserUpdateException;
import com.moiseyev.issuetracker.model.dto.CurrentUserUpdateDto;
import com.moiseyev.issuetracker.model.dto.UserCreateDto;
import com.moiseyev.issuetracker.model.dto.UserResponseDto;
import com.moiseyev.issuetracker.model.dto.UserUpdateDto;
import com.moiseyev.issuetracker.model.entity.Role;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.model.enums.RoleType;
import com.moiseyev.issuetracker.model.mapper.UserMapper;
import com.moiseyev.issuetracker.repository.UserRepository;
import com.moiseyev.issuetracker.security.AuthUserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class UserService {
  private final UserRepository userRepository;
  private final RoleService roleService;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final AuthUserService authUserService;
  private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

  @Autowired
  public UserService(UserRepository userRepository, RoleService roleService, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthUserService authUserService) {
    this.userRepository = userRepository;
    this.roleService = roleService;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.authUserService = authUserService;
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
    if (role.getName() == RoleType.ADMIN) {
      log.warn("attempt to create a user with a role: {}", role.getName());
      throw new RegistrationException("Can not assign the role " + role.getName());
    }
    User user = new User();
    user.setLogin(dto.getLogin());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
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

    if (dto.getPassword() != null && !dto.getPassword().isBlank()
            && !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      user.setPassword(passwordEncoder.encode(dto.getPassword()));
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
  public UserResponseDto updateCurrentUser(CurrentUserUpdateDto dto) {
    User currentUser = authUserService.getCurrentUser();
    boolean hasChanges = false;

    if (dto.getEmail() != null
            && !dto.getEmail().isBlank()
            && !currentUser.getEmail().equals(dto.getEmail())) {
      validateEmailUniqueness(currentUser.getId(), dto.getEmail());
      currentUser.setEmail(dto.getEmail());
      hasChanges = true;
    }

    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
      if (!passwordEncoder.matches(dto.getPassword(), currentUser.getPassword())) {
        if (!dto.getPassword().matches(PASSWORD_PATTERN)) {
          throw new IllegalArgumentException(
                  "Password must contain at least 8 characters, one letter and one digit"
          );
        }
        currentUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        hasChanges = true;
      }
    }

    if (!hasChanges) {
      throw new UserUpdateException(currentUser.getId());
    }

    currentUser.setUpdatedAt(Instant.now());
    return userMapper.toResponseDto(userRepository.save(currentUser));
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
