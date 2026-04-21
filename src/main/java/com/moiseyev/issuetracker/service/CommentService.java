package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.CommentNotFoundException;
import com.moiseyev.issuetracker.model.dto.CommentCreateDto;
import com.moiseyev.issuetracker.model.dto.CommentResponseDto;
import com.moiseyev.issuetracker.model.dto.CommentUpdateDto;
import com.moiseyev.issuetracker.model.entity.Comment;
import com.moiseyev.issuetracker.model.entity.Issue;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.model.mapper.CommentMapper;
import com.moiseyev.issuetracker.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CommentService {
  private final CommentRepository commentRepository;
  private final IssueService issueService;
  private final UserService userService;
  private final CommentMapper commentMapper;

  @Autowired
  public CommentService(CommentRepository commentRepository,
                        IssueService issueService,
                        UserService userService,
                        CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.issueService = issueService;
    this.userService = userService;
    this.commentMapper = commentMapper;
  }

  public CommentResponseDto getCommentById(Long commentId) {
    Comment comment = commentRepository
            .findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
    return commentMapper.toResponseDto(comment);
  }

  public List<CommentResponseDto> getCommentsForIssue(Long issueId) {
    List<CommentResponseDto> comments = commentRepository
            .findByIssueIdOrderByCreatedAtAsc(issueId)
            .stream()
            .map(commentMapper::toResponseDto)
            .toList();
    if (comments.isEmpty()) {
      throw new CommentNotFoundException("Comments for issue with id = " + issueId + " not founds");
    }
    return comments;
  }

  public List<CommentResponseDto> getAllComments() {
    List<CommentResponseDto> commentList = commentRepository
            .findAll()
            .stream()
            .map(commentMapper::toResponseDto)
            .toList();
    if (commentList.isEmpty()) {
      throw new CommentNotFoundException("Comments not found");
    }
    return commentList;
  }

  @Transactional
  public CommentResponseDto createComment(CommentCreateDto commentCreateDto) {
    Issue issue = issueService.getIssueById(commentCreateDto.getIssueId());
    User author = userService.getUserById(commentCreateDto.getAuthorId());

    Comment comment = new Comment();
    comment.setText(commentCreateDto.getText());
    comment.setIssue(issue);
    comment.setAuthor(author);
    comment.setCreatedAt(Instant.now());
    comment = commentRepository.save(comment);
    return commentMapper.toResponseDto(comment);
  }

  @Transactional
  public CommentResponseDto updateComment(CommentUpdateDto commentUpdateDto) {
    Comment commentToUpdate = commentRepository
            .findById(commentUpdateDto.getId())
            .orElseThrow(() -> new CommentNotFoundException(commentUpdateDto.getId()));
    commentToUpdate.setText(commentUpdateDto.getText());
    commentToUpdate = commentRepository.saveAndFlush(commentToUpdate);
    return commentMapper.toResponseDto(commentToUpdate);
  }

  @Transactional
  public void deleteCommentById(Long commentId) {
    Comment comment = commentRepository
            .findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
    commentRepository.delete(comment);
  }
}
