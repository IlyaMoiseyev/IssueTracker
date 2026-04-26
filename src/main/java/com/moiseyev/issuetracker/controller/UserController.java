package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.dto.CurrentUserUpdateDto;
import com.moiseyev.issuetracker.model.dto.UserCreateDto;
import com.moiseyev.issuetracker.model.dto.UserResponseDto;
import com.moiseyev.issuetracker.model.dto.UserUpdateDto;
import com.moiseyev.issuetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    log.info("IN: getAllUsers()");
    List<UserResponseDto> users = userService.getAllUsers();
    log.info("OUT: getAllUsers(). size = {}", users.size());
    return ResponseEntity.ok(users);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
    log.info("IN: getUserById(). Params: id = {}", id);
    UserResponseDto responseDto = userService.getUserById(id);
    log.info("OUT: getUserById(). Result: {}", responseDto);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto dto,
                                                    BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }

    log.info("IN: createUser()");
    UserResponseDto responseDto = userService.save(dto);

    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(responseDto.getId())
            .toUri();

    log.info("OUT: createUser(). Result: {}", responseDto);
    return ResponseEntity.created(location).body(responseDto);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping
  public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto dto,
                                                    BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }

    log.info("IN: updateUser(). Params: id = {}", dto.getId());
    UserResponseDto updatedUser = userService.update(dto);
    log.info("OUT: updateUser().  Result: {}", updatedUser);
    return ResponseEntity.ok(updatedUser);
  }

  @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','REPORTER')")
  @PutMapping("/current")
  public ResponseEntity<UserResponseDto> updateCurrentUser(@Valid @RequestBody CurrentUserUpdateDto currentUserDto) {
    log.info("IN: updateCurrentUser()");
    UserResponseDto updatedCurrentUser = userService.updateCurrentUser(currentUserDto);
    return ResponseEntity.ok(updatedCurrentUser);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("IN: deleteUser(). Params: id = {}", id);
    userService.delete(id);
    log.info("OUT: deleteUser(). Result: User with id = {} deleted", id);
    return ResponseEntity.noContent().build();
  }
}
