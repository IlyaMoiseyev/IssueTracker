package com.moiseyev.issuetracker.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueUpdateDto {
  @NotNull
  private Long id;

  private String title;

  private String description;

  private String statusType;

  private String priorityType;

  private Long assigneeId;
}
