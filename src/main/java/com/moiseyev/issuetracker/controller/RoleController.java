package com.moiseyev.issuetracker.controller;

import com.moiseyev.issuetracker.model.entity.Role;
import com.moiseyev.issuetracker.service.RoleService;
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
@RequestMapping("/role")
public class RoleController {
  private final RoleService roleService;

  @Autowired
  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping
  public ResponseEntity<List<Role>> getAllRoles() {
    log.info("IN: getAllRoles()");
    List<Role> roleList = roleService.findAllRoles();
    log.info("OUT: getAllRoles(). Result: size = {}", roleList.size());
    return ResponseEntity.ok(roleList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Role> getRoleById(@PathVariable Integer id) {
    log.info("IN: getRoleById(). Params: id = {}", id);
    Role role = roleService.getRoleById(id);
    log.info("OUT: getRoleById() Result: role = {}", role);
    return ResponseEntity.ok(role);
  }

  @GetMapping("/name/{roleName}")
  public ResponseEntity<Role> getRoleByName(@PathVariable String roleName) {
    log.info("IN: getRoleByName(). Params: name = {}", roleName);
    Role role = roleService.findRoleByName(roleName);
    log.info("OUT: getRoleByName(). Result: role = {}", role);
    return ResponseEntity.ok(role);
  }
}
