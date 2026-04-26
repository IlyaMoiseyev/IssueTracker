package com.moiseyev.issuetracker.security;

import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
  private final UserRepository userRepository;

  @Autowired
  public CustomUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    User user = userRepository.findUserByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
    return new CustomUserDetails(user);
  }

  public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User with id: " + userId + " not found"));
    return new CustomUserDetails(user);
  }
}
