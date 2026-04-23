package com.moiseyev.issuetracker.repository;

import com.moiseyev.issuetracker.model.entity.Tag;
import com.moiseyev.issuetracker.model.enums.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
  Optional<Tag> findTagByName(TagType name);
}
