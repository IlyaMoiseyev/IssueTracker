package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.dto.UserCreateDto;
import com.moiseyev.issuetracker.model.dto.UserUpdateDto;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    log.info("IN: getAllUsers()");
    List<User> users = userService.getAllUsers();
    log.info("OUT: getAllUsers(). size = {}", users.size());
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    log.info("IN: getUserById(). Params: id = {}", id);
    User user = userService.getUserById(id);
    log.info("OUT: getUserById(). Result: {}", user);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateDto dto,
                                         BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }

    log.info("IN: createUser()");
    User user = userService.save(dto);

    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(user.getId())
            .toUri();

    log.info("OUT: createUser(). Result: {}", user);
    return ResponseEntity.created(location).body(user);
  }

  @PutMapping
  public ResponseEntity<User> updateUser(@Valid @RequestBody UserUpdateDto dto,
                                         BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }

    log.info("IN: updateUser(). Params: id = {}", dto.getId());
    User user = userService.update(dto);
    log.info("OUT: updateUser().  Result: {}", user);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("IN: deleteUser(). Params: id = {}", id);
    userService.delete(id);
    log.info("OUT: deleteUser(). Result: User with id = {} deleted", id);
    return ResponseEntity.noContent().build();
  }
}
