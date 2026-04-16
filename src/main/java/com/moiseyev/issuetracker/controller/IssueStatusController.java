package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.entity.IssueStatus;
import com.moiseyev.issuetracker.service.IssueStatusService;
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
@RequestMapping("/status")
public class IssueStatusController {
  private final IssueStatusService issueStatusService;

  @Autowired
  public IssueStatusController(IssueStatusService issueStatusService) {
    this.issueStatusService = issueStatusService;
  }

  @GetMapping
  public ResponseEntity<List<IssueStatus>> getAllIssueStatuses() {
    log.info("IN: getAllIssueStatuses()");
    List<IssueStatus> issueStatusList = issueStatusService.findAllIssueStatuses();
    log.info("OUT: getAllIssueStatuses(). Result: size = {}", issueStatusList.size());
    return ResponseEntity.ok(issueStatusList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<IssueStatus> getIssueStatusById(@PathVariable Integer id) {
    log.info("IN: getIssueStatusById(). Param: id = {}", id);
    IssueStatus issueStatus = issueStatusService.getIssueStatusById(id);
    log.info("OUT: getIssueStatusById(). Result: issueStatus = {}", issueStatus);
    return ResponseEntity.ok(issueStatus);
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<IssueStatus> getIssueStatusByNameByName(@PathVariable String name) {
    log.info("IN: getIssueStatusByNameByName(). Param: name = {}", name);
    IssueStatus issueStatus = issueStatusService.getIssueStatusByName(name);
    log.info("OUT: getIssueStatusByNameByName(). Result: issueStatus = {}", issueStatus);
    return ResponseEntity.ok(issueStatus);
  }
}
