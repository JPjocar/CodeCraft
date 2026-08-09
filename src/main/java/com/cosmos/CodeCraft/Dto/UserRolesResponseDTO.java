package com.cosmos.CodeCraft.Dto;

import java.util.Set;

public record UserRolesResponseDTO(
        Long id,
        String username,
        Set<String> roles) {

}
