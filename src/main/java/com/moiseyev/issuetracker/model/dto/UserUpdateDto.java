package com.moiseyev.issuetracker.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
  @NotNull
  private Long id;

  @NotBlank
  private String login;

  @Email
  private String email;

  @NotBlank
  private String password;
}
