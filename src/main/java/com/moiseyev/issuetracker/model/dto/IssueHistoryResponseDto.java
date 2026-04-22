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
public class IssueHistoryResponseDto {
  private Long historyId;
  private Long issueId;
  private String fieldName;
  private String oldValue;
  private String newValue;
  private Long changedBy;
  private Instant changedAt;
}
