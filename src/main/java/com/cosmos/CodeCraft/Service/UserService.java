/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Entity.UserEntity;
import com.cosmos.CodeCraft.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void create(UserEntity user) {
        this.userRepository.save(user);
    }

    public UserEntity get(Long id) {
        return this.userRepository.findById(id).get();
    }
    
    
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }
}
