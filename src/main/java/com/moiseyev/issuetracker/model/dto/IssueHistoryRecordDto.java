package com.moiseyev.issuetracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueHistoryRecordDto {
  @NotNull
  private Long issueId;

  @NotBlank
  private String fieldName;

  @NotBlank
  private String oldValue;

  @NotBlank
  private String newValue;

  @NotNull
  private Long changedBy;
}
