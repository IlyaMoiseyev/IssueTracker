package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.entity.IssuePriority;
import com.moiseyev.issuetracker.service.IssuePriorityService;
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
@RequestMapping("/priority")
public class IssuePriorityController {
  private final IssuePriorityService issuePriorityService;

  @Autowired
  public IssuePriorityController(IssuePriorityService issuePriorityService) {
    this.issuePriorityService = issuePriorityService;
  }

  @GetMapping
  public ResponseEntity<List<IssuePriority>> getAllIssuePriorities() {
    log.info("IN: getAllIssuePriorities()");
    List<IssuePriority> issuePriorityList = issuePriorityService.findAllIssuePriorities();
    log.info("OUT: getAllIssuePriorities(). Result: size = {}", issuePriorityList.size());
    return ResponseEntity.ok(issuePriorityList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<IssuePriority> getIssuePriorityById(@PathVariable Integer id) {
    log.info("IN: getIssuePriorityById(). Param: id = {}", id);
    IssuePriority issuePriority = issuePriorityService.getIssuePriorityById(id);
    log.info("OUT: getIssuePriorityById(). Result: issuePriority = {}", issuePriority);
    return ResponseEntity.ok(issuePriority);
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<IssuePriority> getIssuePriorityByName(@PathVariable String name) {
    log.info("IN: getIssuePriorityByName(). Param: name = {}", name);
    IssuePriority issuePriority = issuePriorityService.getIssuePriorityByName(name);
    log.info("OUT: getIssuePriorityByName(). Result: issuePriority = {}", issuePriority);
    return ResponseEntity.ok(issuePriority);
  }
}
