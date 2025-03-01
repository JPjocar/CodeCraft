/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Entity.RoleEntity;
import com.cosmos.CodeCraft.Entity.TagEntity;
import com.cosmos.CodeCraft.Entity.UserEntity;
import com.cosmos.CodeCraft.Repository.RoleRepository;
import com.cosmos.CodeCraft.Repository.TagRepository;
import com.cosmos.CodeCraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TagRepository tagRepository;
    
    @PostMapping
    public void create(@RequestBody UserEntity userEntity) {
        this.userService.create(userEntity);
    }

    @GetMapping("/{id}")
    public UserEntity get(@PathVariable("id") Long id) {
        return this.userService.get(id);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }
    
    
    @PostMapping("/role")
    public void createRole(@RequestBody RoleEntity role) {
        this.roleRepository.save(role);
    }
    
    @PostMapping("/create-tag")
    public void createTag(@RequestBody TagEntity tag) {
        this.tagRepository.save(tag);
    }
    
}
