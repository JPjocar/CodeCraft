/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    /** Longitud minima para una clave HMAC-SHA256 (256 bits). */
    private static final int MIN_KEY_LENGTH = 32;

    @Value("${spring.jwt.private.key}")
    private String privateKey;

    @Value("${spring.jwt.private.user}")
    private String userGenerator;

    /**
     * Falla al arrancar si la clave es debil o no esta configurada. Es preferible
     * que la aplicacion no levante a que firme tokens con un secreto adivinable.
     */
    @PostConstruct
    void validateSigningKey() {
        if (privateKey == null || privateKey.isBlank() || privateKey.length() < MIN_KEY_LENGTH) {
            throw new IllegalStateException(
                    "La clave de firma JWT debe tener al menos " + MIN_KEY_LENGTH
                    + " caracteres. Define la variable de entorno JWT_SECRET.");
        }
    }

    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withNotBefore(new Date(System.currentTimeMillis()))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Invalid token");
        }
    }

    
    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();
    }
    
    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
    
    public Claim getSpecifyClaim(DecodedJWT decodedJWT, String claim){
        return decodedJWT.getClaim(claim);
    }
}
