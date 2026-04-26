package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.dto.IssueResponseDto;
import com.moiseyev.issuetracker.model.entity.Tag;
import com.moiseyev.issuetracker.service.IssueTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/issues")
public class IssueTagController {
  private final IssueTagService issueTagService;

  @Autowired
  public IssueTagController(IssueTagService issueTagService) {
    this.issueTagService = issueTagService;
  }

  @GetMapping("/{issueId}/tag")
  @PreAuthorize("@accessService.canViewTags(#issueId)")
  public ResponseEntity<List<Tag>> getTagsForIssue(@PathVariable Long issueId) {
    log.info("IN: getTagsForIssue(). Params: issueId = {}", issueId);
    List<Tag> tags = issueTagService.getTagsForIssue(issueId);
    log.info("OUT: getTagsForIssue(). Result: list size = {}", tags.size());
    return ResponseEntity.ok(tags);
  }

  @GetMapping("/search/tag/{tagName}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<IssueResponseDto>> getIssuesByTag(@PathVariable String tagName) {
    log.info("IN: getIssuesByTag(). Params: tagName = {}", tagName);
    List<IssueResponseDto> issuesList = issueTagService.getIssuesByTag(tagName);
    log.info("OUT: getIssuesByTag(). Result: list size = {}", issuesList.size());
    return ResponseEntity.ok(issuesList);
  }

  @PostMapping("/{issueId}/tag/{tagName}")
  @PreAuthorize("@accessService.canManageTags(#issueId)")
  public ResponseEntity<Void> addTagToIssue(@PathVariable Long issueId, @PathVariable String tagName) {
    log.info("IN: addTagToIssue(). Params: issueId = {}, tag = {}", issueId, tagName);
    issueTagService.addTagToIssue(issueId, tagName);
    log.info("OUT: addTagToIssue(). Result: added tag = {} to issue with id {}", tagName, issueId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{issueId}/tag/{tagName}")
  @PreAuthorize("@accessService.canManageTags(#issueId)")
  public ResponseEntity<Void> removeTagFromIssue(@PathVariable Long issueId, @PathVariable String tagName) {
    log.info("IN: removeTagFromIssue(). Params: issueId = {}, tag = {}", issueId, tagName);
    issueTagService.removeTagFromIssue(issueId, tagName);
    log.info("OUT: removeTagFromIssue(). Result: removed tag = {} from issue with id {}", tagName, issueId);
    return ResponseEntity.noContent().build();
  }
}
