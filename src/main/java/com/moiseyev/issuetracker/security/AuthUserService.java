package com.moiseyev.issuetracker.security;

import com.moiseyev.issuetracker.exception.AuthenticationException;
import com.moiseyev.issuetracker.exception.UserNotFoundException;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUserService {
  private final UserRepository userRepository;

  @Autowired
  public AuthUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails principal)) {
      throw new AuthenticationException("Unauthenticated");
    }
    Long userId = principal.getId();
    return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
  }

  public Long getCurrentUserId() {
    return getCurrentUser().getId();
  }
}
