package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.dto.IssueHistoryResponseDto;
import com.moiseyev.issuetracker.service.IssueHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/history")
public class IssueHistoryController {
  private final IssueHistoryService issueHistoryService;

  @Autowired
  public IssueHistoryController(IssueHistoryService issueHistoryService) {
    this.issueHistoryService = issueHistoryService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<IssueHistoryResponseDto>> getAllHistoryRecords() {
    log.info("IN: getAllHistoryRecords()");
    List<IssueHistoryResponseDto> historyRecordsList = issueHistoryService.getAllHistoryRecords();
    log.info("OUT: getAllHistoryRecords(). Result: size {}", historyRecordsList.size());
    return ResponseEntity.ok(historyRecordsList);
  }

  @GetMapping("{historyId}")
  @PreAuthorize("@accessService.canViewHistoryRecord(#historyId)")
  public ResponseEntity<IssueHistoryResponseDto> getHistoryById(@PathVariable Long historyId) {
    log.info("IN: getHistoryById(). Params: historyId = {}", historyId);
    IssueHistoryResponseDto issueHistoryResponseDto = issueHistoryService.getHistoryById(historyId);
    log.info("OUT: getHistoryById(). Result: {}", issueHistoryResponseDto);
    return ResponseEntity.ok(issueHistoryResponseDto);
  }

  @GetMapping("/issue/{issueId}")
  @PreAuthorize("@accessService.canViewHistoryForIssue(#issueId)")
  public ResponseEntity<List<IssueHistoryResponseDto>> getAllHistoryForIssue(@PathVariable Long issueId) {
    log.info("IN: getAllHistoryForIssue(). Params: issueId = {}", issueId);
    List<IssueHistoryResponseDto> issueHistoryList = issueHistoryService.getHistoryForIssue(issueId);
    log.info("OUT: getAllHistoryForIssue(). Result: listSize = {}", issueHistoryList.size());
    return ResponseEntity.ok(issueHistoryList);
  }
}
