package com.moiseyev.issuetracker.model.mapper;

import com.moiseyev.issuetracker.model.dto.CommentResponseDto;
import com.moiseyev.issuetracker.model.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
  public CommentResponseDto toResponseDto(Comment comment) {
    return CommentResponseDto.builder()
            .id(comment.getId())
            .text(comment.getText())
            .issueId(comment.getIssue().getId())
            .authorId(comment.getAuthor().getId())
            .authorLogin(comment.getAuthor().getLogin())
            .authorEmail(comment.getAuthor().getEmail())
            .createdAt(comment.getCreatedAt())
            .build();
  }
}
