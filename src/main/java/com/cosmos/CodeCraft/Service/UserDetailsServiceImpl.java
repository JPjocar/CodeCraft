/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.AuthCreateRequest;
import com.cosmos.CodeCraft.Dto.AuthLoginRequest;
import com.cosmos.CodeCraft.Dto.AuthResponse;
import com.cosmos.CodeCraft.Dto.UserRolesResponseDTO;
import com.cosmos.CodeCraft.Entity.RoleEntity;
import com.cosmos.CodeCraft.Entity.UserEntity;
import com.cosmos.CodeCraft.Exception.ResourceNotFoundException;
import com.cosmos.CodeCraft.Exception.UsernameAlreadyExistsException;
import com.cosmos.CodeCraft.Repository.RoleRepository;
import com.cosmos.CodeCraft.Repository.UserRepository;
import com.cosmos.CodeCraft.Utils.JwtUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    /** Unico rol que se concede en el registro publico. */
    private static final String DEFAULT_ROLE = "USER";

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RoleRepository roleRepository;

    public UserEntity save(UserEntity user){
        return this.userRepository.save(user);
    }

    public UserEntity findByUsername(String username){
        return this.userRepository.findUserEntityByUsername(username).orElseThrow();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow( () -> new ResourceNotFoundException("User", "username", username) );
        
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        userEntity.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName()))));
        
        userEntity.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        
        
        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNonExpired(), 
                userEntity.isCredentialsNonExpired(),
                userEntity.isAccountNonLocked(),
                authorities);
    }
    
    
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest){
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.validate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = jwtUtils.createToken(authentication);
        
        return new AuthResponse(username, "User logged!", token, true);
    }
    
    public Authentication validate(String username, String password){
        UserDetails userDetails = loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException(username+" not found");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException(username+" bad password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    // Crea un usuario con valor DEFAULT_ROLE por defecto
    @Transactional
    public AuthResponse create(AuthCreateRequest authCreateRequest){
        String username = authCreateRequest.username();
        String password = authCreateRequest.password();

        if(this.userRepository.findUserEntityByUsername(username).isPresent()){
            throw new UsernameAlreadyExistsException(username);
        }

        Set<RoleEntity> roles = this.roleRepository.findRoleEntityByNameIn(Set.of(DEFAULT_ROLE));

        if(roles.isEmpty()){
            throw new IllegalStateException(
                    "El rol por defecto '" + DEFAULT_ROLE + "' no existe en la base de datos");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(this.passwordEncoder.encode(password))
                .roles(roles)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
        
        UserEntity userCreated = this.userRepository.save(userEntity);
        
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userCreated.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName()))));
        userCreated.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userCreated.getPassword(), authorities);

        String token = this.jwtUtils.createToken(authentication);
        return new AuthResponse(userCreated.getUsername(), "User created", token, true);
    }

    // Asignar roles a los usuarios, se ejecuta despues de loggin o despues de refresh token pero hay que chequearlo
    @Transactional
    public UserRolesResponseDTO assignRoles(String username, Set<String> roleNames){
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Set<RoleEntity> roles = this.roleRepository.findRoleEntityByNameIn(roleNames);

        // Si algun rol de rolNames no existe debera lanzar un error, es mejor lanzar un error que quitar persmisos sin avisar
        if(roles.size() != roleNames.size()){
            Set<String> found = roles.stream().map(RoleEntity::getName).collect(Collectors.toSet());
            Set<String> unknown = roleNames.stream().filter(role -> !found.contains(role)).collect(Collectors.toSet());
            throw new ResourceNotFoundException("Role", "name", String.join(", ", unknown));
        }

        userEntity.setRoles(roles);
        UserEntity updated = this.userRepository.save(userEntity);

        return new UserRolesResponseDTO(
                updated.getId(),
                updated.getUsername(),
                updated.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()));
    }
}
