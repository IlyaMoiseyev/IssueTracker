package com.moiseyev.issuetracker.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ManyToOne(optional = false)
  @JoinColumn(name = "issue_id", nullable = false)
  private Issue issue;

  @ManyToOne(optional = false)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;
}
