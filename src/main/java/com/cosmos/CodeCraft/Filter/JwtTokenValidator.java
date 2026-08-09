/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cosmos.CodeCraft.Utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtTokenValidator extends OncePerRequestFilter{

    private static final String BEARER_PREFIX = "Bearer ";

    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Sin cabecera, o con un esquema que no es Bearer (Basic, Digest...), la
        // peticion sigue sin autenticar: seran las reglas de autorizacion quienes
        // decidan si el endpoint es publico. Antes se hacia substring(7) a ciegas,
        // lo que reventaba con StringIndexOutOfBoundsException -> 500.
        if(header == null || !header.startsWith(BEARER_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();

        try {
            DecodedJWT decodedJWT = this.jwtUtils.verifyToken(token);

            String username = this.jwtUtils.extractUsername(decodedJWT);

            String authoritiesString = this.jwtUtils.getSpecifyClaim(decodedJWT, "authorities").asString();

            Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString);

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JWTVerificationException ex) {
            // Los filtros se ejecutan ANTES del DispatcherServlet, asi que
            // GlobalExceptionHandler (@RestControllerAdvice) nunca ve esta
            // excepcion. Si la dejaramos propagar, un token invalido devolveria
            // un 500 con stacktrace en vez de un 401. La escribimos a mano.
            SecurityContextHolder.clearContext();
            writeUnauthorized(response, ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"errorCode\":\"Invalid JWT\",\"message\":\"" + escapeJson(message) + "\"}");
    }

    private String escapeJson(String value){
        if(value == null){
            return "Token invalido";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
