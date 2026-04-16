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
@Table(name = "issues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(name = "status_id", nullable = false)
  private IssueStatus status;

  @ManyToOne(optional = false)
  @JoinColumn(name = "priority_id")
  private IssuePriority priority;

  @ManyToOne(optional = false)
  @JoinColumn(name = "reporter_id", nullable = false)
  private User reporter;

  @ManyToOne()
  @JoinColumn(name = "assignee_id")
  private User assignee;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;
}
