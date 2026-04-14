package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.EmailAlreadyExistsException;
import com.moiseyev.issuetracker.exception.LoginAlreadyExistsException;
import com.moiseyev.issuetracker.exception.UserNotFoundException;
import com.moiseyev.issuetracker.model.dto.UserCreateDto;
import com.moiseyev.issuetracker.model.dto.UserUpdateDto;
import com.moiseyev.issuetracker.model.entity.Role;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.model.enums.RoleType;
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

  @Autowired
  public UserService(UserRepository userRepository, RoleService roleService) {
    this.userRepository = userRepository;
    this.roleService = roleService;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUserById(Long id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
  }

  @Transactional
  public User save(UserCreateDto dto) {
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
    return userRepository.save(user);
  }

  @Transactional
  public User update(UserUpdateDto dto) {
    User user = userRepository.findById(dto.getId())
            .orElseThrow(() -> new UserNotFoundException(dto.getId()));

    validateUniqueFields(dto.getId(), dto.getLogin(), dto.getEmail());

    user.setLogin(dto.getLogin());
    user.setEmail(dto.getEmail());
    user.setPassword(dto.getPassword());
    user.setUpdatedAt(Instant.now());

    log.info("Updating user with id = {}", dto.getId());
    return userRepository.saveAndFlush(user);
  }

  @Transactional
  public void delete(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

    log.info("Deleting user with id = {}", id);
    userRepository.delete(user);
  }

  private void validateUniqueFields(Long excludeId, String login, String email) {
    if (loginIsTaken(excludeId, login)) {
      throw new LoginAlreadyExistsException(login);
    }
    if (emailIsTaken(excludeId, email)) {
      throw new EmailAlreadyExistsException(email);
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
