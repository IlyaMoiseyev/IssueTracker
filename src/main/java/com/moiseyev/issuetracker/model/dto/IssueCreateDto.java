package com.moiseyev.issuetracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueCreateDto {
  @NotBlank
  private String title;

  private String description;

  @NotBlank
  private String status;

  @NotBlank
  private String priority;

  private Long assigneeId;
}
