package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.entity.IssueHistory;
import com.moiseyev.issuetracker.service.IssueHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/issue/history")
public class IssueHistoryController {
  private final IssueHistoryService issueHistoryService;

  @Autowired
  public IssueHistoryController(IssueHistoryService issueHistoryService) {
    this.issueHistoryService = issueHistoryService;
  }

  @GetMapping("/{issueId}")
  public ResponseEntity<List<IssueHistory>> getAllHistoryForIssue(@PathVariable Long issueId) {
    log.info("IN: getAllHistoryForIssue(). Params: issueId = {}", issueId);
    List<IssueHistory> issueHistoryList = issueHistoryService.getHistoryForIssue(issueId);
    log.info("OUT: getAllHistoryForIssue(). Result: listSize = {}", issueHistoryList.size());
    return ResponseEntity.ok(issueHistoryList);
  }
}
