package com.moiseyev.issuetracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateDto {
  @NotNull
  private Long issueId;

  @NotBlank
  private String text;
}
