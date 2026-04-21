package com.moiseyev.issuetracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
  private Long id;
  private String text;
  private Long issueId;
  private Long authorId;
  private String authorLogin;
  private String authorEmail;
  private Instant createdAt;
}
