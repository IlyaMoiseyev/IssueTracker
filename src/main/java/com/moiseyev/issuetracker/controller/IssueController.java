package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.dto.IssueCreateDto;
import com.moiseyev.issuetracker.model.dto.IssueUpdateDto;
import com.moiseyev.issuetracker.model.entity.Issue;
import com.moiseyev.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/issue")
public class IssueController {
  private final IssueService issueService;

  @Autowired
  public IssueController(IssueService issueService) {
    this.issueService = issueService;
  }

  @GetMapping
  public ResponseEntity<List<Issue>> getAllIssues() {
    log.info("IN: getAllIssues().");
    List<Issue> issueList = issueService.getAllIssues();
    log.info("OUT: getAllIssues().  Result: size{}", issueList.size());
    return ResponseEntity.ok(issueList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Issue> getIssueById(@PathVariable Long id) {
    log.info("IN: getIssueById(). Params: id = {}", id);
    Issue issue = issueService.getIssueById(id);
    log.info("OUT: getIssueById(). Result: {}", issue);
    return ResponseEntity.ok(issue);
  }

  @PostMapping
  public ResponseEntity<Issue> createIssue(@RequestBody @Valid IssueCreateDto issueCreateDto,
                                           BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }

    log.info("IN: createIssue()");
    Issue createdIssue = issueService.createIssue(issueCreateDto);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdIssue.getId())
            .toUri();
    log.info("OUT: createIssue(). Result: created issue with id = {}", createdIssue.getId());
    return ResponseEntity.created(location).body(createdIssue);
  }

  @PutMapping
  public ResponseEntity<Issue> updateIssue(@RequestBody @Valid IssueUpdateDto issueUpdateDto,
                                           BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }

    log.info("IN: updateIssue(). Params: id = {}", issueUpdateDto.getId());
    Issue updatedIssue = issueService.updateIssue(issueUpdateDto);
    log.info("OUT: updateIssue(). Result: issue with id = {} updated", updatedIssue.getId());
    return ResponseEntity.ok(updatedIssue);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteIssueById(@PathVariable Long id) {
    log.info("IN: deleteIssueById(). Params: id = {}", id);
    issueService.deleteIssueById(id);
    log.info("OUT: deleteIssueById(). Result: delete issue with id = {}", id);
    return ResponseEntity.noContent().build();
  }
}
