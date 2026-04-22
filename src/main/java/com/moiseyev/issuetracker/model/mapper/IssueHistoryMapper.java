package com.moiseyev.issuetracker.model.mapper;

import com.moiseyev.issuetracker.model.dto.IssueHistoryResponseDto;
import com.moiseyev.issuetracker.model.entity.IssueHistory;
import org.springframework.stereotype.Component;

@Component
public class IssueHistoryMapper {
  public IssueHistoryResponseDto toResponseDto(IssueHistory issueHistory) {
    return IssueHistoryResponseDto.builder()
            .historyId(issueHistory.getId())
            .issueId(issueHistory.getIssue().getId())
            .fieldName(issueHistory.getFieldName())
            .oldValue(issueHistory.getOldValue())
            .newValue(issueHistory.getNewValue())
            .changedBy(issueHistory.getChangedBy().getId())
            .changedAt(issueHistory.getChangedAt())
            .build();
  }
}
