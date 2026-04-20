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
@Table(name = "issue_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "issue_id")
  private Issue issue;

  @Column(name = "field_name", nullable = false, length = 50)
  private String fieldName;

  @Column(name = "old_value")
  private String oldValue;

  @Column(name = "new_value")
  private String newValue;

  @ManyToOne(optional = false)
  @JoinColumn(name = "changed_by", nullable = false)
  private User changedBy;

  @Column(name = "changed_at", nullable = false)
  private Instant changedAt;
}
