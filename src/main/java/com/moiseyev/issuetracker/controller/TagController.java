package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.entity.Tag;
import com.moiseyev.issuetracker.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tag")
public class TagController {
  private final TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  public ResponseEntity<List<Tag>> getAllTags() {
    log.info("IN: getAllTags()");
    List<Tag> tagList = tagService.findAllTags();
    log.info("OUT: getAllTags(). Result: size = {}", tagList.size());
    return ResponseEntity.ok(tagList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tag> getTagById(@PathVariable Integer id) {
    log.info("IN: getTagById(). Params: id = {}", id);
    Tag tag = tagService.getTagById(id);
    log.info("OUT: getTagById() Result: tag = {}", tag);
    return ResponseEntity.ok(tag);
  }

  @GetMapping("/name/{tagName}")
  public ResponseEntity<Tag> getTagByName(@PathVariable String tagName) {
    log.info("IN: getTagByName(). Params: name = {}", tagName);
    Tag tag = tagService.findTagByName(tagName);
    log.info("OUT: getTagByName(). Result: tag = {}", tag);
    return ResponseEntity.ok(tag);
  }
}
