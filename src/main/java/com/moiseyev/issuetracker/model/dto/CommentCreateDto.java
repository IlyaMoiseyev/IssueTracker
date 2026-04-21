package com.moiseyev.issuetracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateDto {
  @NotBlank
  private String text;

  @NotNull
  private Long issueId;

  @NotNull
  private Long authorId;
}
