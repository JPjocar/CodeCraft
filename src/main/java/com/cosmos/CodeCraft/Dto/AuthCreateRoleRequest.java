/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.springframework.validation.annotation.Validated;

/**
 * Roles a asignar a un usuario. Solo se acepta desde endpoints de administracion
 * (PUT /admin/users/{username}/roles); el registro publico nunca lee este DTO.
 *
 * @author Cosmos
 */
@Validated
public record AuthCreateRoleRequest(
        @NotNull @Size(min = 1, max = 3, message = "Solo debe existir como max 3 roles") Set<String> roles) {

}
