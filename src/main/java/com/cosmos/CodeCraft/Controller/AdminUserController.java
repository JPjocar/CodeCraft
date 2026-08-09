package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Dto.AuthCreateRoleRequest;
import com.cosmos.CodeCraft.Dto.UserRolesResponseDTO;
import com.cosmos.CodeCraft.Service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Seguridad doble con preAuthorize y restricciones en securityConfig
@RestController
@RequestMapping(path = "/admin/users")
public class AdminUserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @PutMapping("/{username}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserRolesResponseDTO> assignRoles(
            @PathVariable("username") String username,
            @RequestBody @Valid AuthCreateRoleRequest authCreateRoleRequest) {

        UserRolesResponseDTO response = this.userDetailsServiceImpl.assignRoles(
                username, authCreateRoleRequest.roles());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
