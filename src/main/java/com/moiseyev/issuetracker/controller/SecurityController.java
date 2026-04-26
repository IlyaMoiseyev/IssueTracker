package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.exception.UserNotFoundException;
import com.moiseyev.issuetracker.model.dto.AuthRequestDto;
import com.moiseyev.issuetracker.model.dto.AuthResponseDto;
import com.moiseyev.issuetracker.model.dto.TokenResponseDto;
import com.moiseyev.issuetracker.model.dto.UserCreateDto;
import com.moiseyev.issuetracker.model.dto.UserResponseDto;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.repository.UserRepository;
import com.moiseyev.issuetracker.security.JwtService;
import com.moiseyev.issuetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final UserService userService;

  @Autowired
  public SecurityController(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            JwtService jwtService, UserService userService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequestDto authRequestDto) {

    User user = userRepository.findUserByLogin(authRequestDto.getLogin())
            .orElseThrow(() -> new UserNotFoundException("User with login " + authRequestDto.getLogin() + " not found"));

    if (!passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid login or password");
    }

    String token = jwtService.generateToken(
            user.getId(),
            user.getRole().getName().name()
    );
    return ResponseEntity.ok(new AuthResponseDto(token));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto userCreateDto,
                                    BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      String errorMessage = bindingResult.getAllErrors().stream()
              .map(error -> error.getDefaultMessage())
              .findFirst()
              .orElse("Validation failed");
      return ResponseEntity.badRequest().body(errorMessage);
    }

    UserResponseDto userResponse = userService.save(userCreateDto);
    String token = jwtService.generateToken(
            userResponse.getId(),
            userResponse.getRole()
    );

    return ResponseEntity.ok(new TokenResponseDto(token));
  }
}
