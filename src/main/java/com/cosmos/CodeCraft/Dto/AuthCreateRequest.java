/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record AuthCreateRequest(
        @NotBlank @Size(min = 3, max = 30) String username,
        @NotBlank @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres") String password) {
}
