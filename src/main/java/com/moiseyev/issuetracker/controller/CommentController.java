package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.dto.CommentCreateDto;
import com.moiseyev.issuetracker.model.dto.CommentResponseDto;
import com.moiseyev.issuetracker.model.dto.CommentUpdateDto;
import com.moiseyev.issuetracker.service.CommentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/comment")
public class CommentController {
  private final CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @GetMapping("{id}")
  @PreAuthorize("@accessService.canViewComment(#id)")
  public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
    log.info("IN: getCommentById(). Params: id = {}", id);
    CommentResponseDto comment = commentService.getCommentById(id);
    log.info("OUT: getCommentById(). Result: comment = {}", comment);
    return ResponseEntity.ok(comment);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<CommentResponseDto>> getAllComments() {
    log.info("IN: getAllComments()");
    List<CommentResponseDto> allComments = commentService.getAllComments();
    return ResponseEntity.ok(allComments);
  }

  @GetMapping("/issue/{issueId}")
  @PreAuthorize("@accessService.canViewIssue(#issueId)")
  public ResponseEntity<List<CommentResponseDto>> getAllCommentsForIssue(@PathVariable Long issueId) {
    log.info("IN: getAllCommentsForIssue(). Params: issueId = {}", issueId);
    List<CommentResponseDto> commentList = commentService.getCommentsForIssue(issueId);
    log.info("OUT: getAllCommentsForIssue(). Result: list size = {}", commentList.size());
    return ResponseEntity.ok(commentList);
  }

  @PostMapping
  public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentCreateDto commentCreateDto,
                                                          BindingResult bindingResult) {
    log.info("IN: createComment(). Params: comment for issue with id = {}", commentCreateDto.getIssueId());
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }
    CommentResponseDto comment = commentService.createComment(commentCreateDto);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(comment.getId())
            .toUri();
    log.info("OUT: createComment(). Result: created comment, id = {}", comment.getId());
    return ResponseEntity.created(location).body(comment);
  }

  @PutMapping
  @PreAuthorize("@accessService.canModifyComment(#commentUpdateDto.id)")
  public ResponseEntity<CommentResponseDto> updateComment(@RequestBody @Valid CommentUpdateDto commentUpdateDto,
                                                          BindingResult bindingResult) {
    log.info("IN: updateComment(). Params: id = {}", commentUpdateDto.getId());
    if (bindingResult.hasErrors()) {
      log.warn("Validation failed: {}", bindingResult.getAllErrors());
      return ResponseEntity.badRequest().build();
    }
    CommentResponseDto updatedComment = commentService.updateComment(commentUpdateDto);
    log.info("OUT: updateComment(). Result: updated comment, id = {}", updatedComment.getId());
    return ResponseEntity.ok(updatedComment);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("@accessService.canModifyComment(#id)")
  public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
    log.info("IN: deleteComment(). Params: deleting comment with id = {}", id);
    commentService.deleteCommentById(id);
    log.info("OUT: deleteComment(). Result: comment with id = {} deleted", id);
    return ResponseEntity.ok().build();
  }
}
