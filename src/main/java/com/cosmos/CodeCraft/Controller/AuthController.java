/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Dto.AuthCreateRequest;
import com.cosmos.CodeCraft.Dto.AuthLoginRequest;
import com.cosmos.CodeCraft.Dto.AuthResponse;
import com.cosmos.CodeCraft.Service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    
    @PostMapping("/log-in")
    public AuthResponse login(@RequestBody @Valid AuthLoginRequest authLoginRequest){
        System.out.println("eje");
        return this.userDetailsServiceImpl.loginUser(authLoginRequest);
    }
    
    @PostMapping("/sign-up")
    public AuthResponse register(@RequestBody @Valid AuthCreateRequest authCreateRequest){
        return this.userDetailsServiceImpl.create(authCreateRequest);
    }
}
