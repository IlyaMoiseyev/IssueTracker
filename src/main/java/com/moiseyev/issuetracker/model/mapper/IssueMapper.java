package com.moiseyev.issuetracker.model.mapper;

import com.moiseyev.issuetracker.model.dto.IssueResponseDto;
import com.moiseyev.issuetracker.model.entity.Issue;
import org.springframework.stereotype.Component;

@Component
public class IssueMapper {
  public IssueResponseDto toResponseDto(Issue issue) {
    return IssueResponseDto.builder()
            .id(issue.getId())
            .title(issue.getTitle())
            .status(issue.getStatus().getName().toString())
            .priority(issue.getPriority().getName().toString())
            .reporterId(issue.getReporter().getId())
            .assigneeId(issue.getAssignee() != null ? issue.getAssignee().getId() : null)
            .createdAt(issue.getCreatedAt())
            .updatedAt(issue.getUpdatedAt())
            .build();
  }
}
