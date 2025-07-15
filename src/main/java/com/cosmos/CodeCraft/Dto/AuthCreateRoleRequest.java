/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Dto;

import jakarta.validation.constraints.Size;
import java.util.Set;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author Cosmos
 */
@Validated
public record AuthCreateRoleRequest(
        @Size(min = 1, max = 3, message = "Solo debe existir como max 3 roles") Set<String> roles) {
    
}
