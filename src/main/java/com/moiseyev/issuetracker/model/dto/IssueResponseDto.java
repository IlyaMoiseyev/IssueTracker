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
public class IssueResponseDto {
  private Long id;
  private String title;
  private String status;
  private String priority;
  private Long reporterId;
  private Long assigneeId;
  private Instant createdAt;
  private Instant updatedAt;
}
