package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.dto.IssueCreateDto;
import com.moiseyev.issuetracker.model.dto.IssueResponseDto;
import com.moiseyev.issuetracker.model.dto.IssueUpdateDto;
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
  public ResponseEntity<List<IssueResponseDto>> getAllIssues() {
    log.info("IN: getAllIssues()");
    List<IssueResponseDto> issueList = issueService.getAllIssues();
    log.info("OUT: getAllIssues().  Result: list size = {} ", issueList.size());
    return ResponseEntity.ok(issueList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<IssueResponseDto> getIssueById(@PathVariable Long id) {
    log.info("IN: getIssueById(). Params: id = {}", id);
    IssueResponseDto issue = issueService.getIssueById(id);
    log.info("OUT: getIssueById(). Result: {}", issue);
    return ResponseEntity.ok(issue);
  }

  @PostMapping
  public ResponseEntity<IssueResponseDto> createIssue(@RequestBody @Valid IssueCreateDto issueCreateDto,
                                                      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }

    log.info("IN: createIssue()");
    IssueResponseDto issueResponseDto = issueService.createIssue(issueCreateDto);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(issueResponseDto.getId())
            .toUri();
    log.info("OUT: createIssue(). Result: created issue with id = {}", issueResponseDto.getId());
    return ResponseEntity.created(location).body(issueResponseDto);
  }

  @PutMapping
  public ResponseEntity<IssueResponseDto> updateIssue(@RequestBody @Valid IssueUpdateDto issueUpdateDto,
                                                      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }

    log.info("IN: updateIssue(). Params: id = {}", issueUpdateDto.getId());
    IssueResponseDto issueResponseDto = issueService.updateIssue(issueUpdateDto);
    log.info("OUT: updateIssue(). Result: issue with id = {} updated", issueResponseDto.getId());
    return ResponseEntity.ok(issueResponseDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteIssueById(@PathVariable Long id) {
    log.info("IN: deleteIssueById(). Params: id = {}", id);
    issueService.deleteIssueById(id);
    log.info("OUT: deleteIssueById(). Result: delete issue with id = {}", id);
    return ResponseEntity.noContent().build();
  }
}
