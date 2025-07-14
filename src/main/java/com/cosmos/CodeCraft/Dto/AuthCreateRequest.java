/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Cosmos
 */
public record AuthCreateRequest(
        @NotNull String username,
        @NotNull String password,
        @Valid AuthCreateRoleRequest authCreateRoleRequest ) {
    
}
