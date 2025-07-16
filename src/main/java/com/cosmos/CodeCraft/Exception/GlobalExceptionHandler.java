/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Resource not found", ex.getMessage()));
    }
    
    @ExceptionHandler(InsufficientTagsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientTags(InsufficientTagsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Tags required max=5", ex.getMessage()));
    }
    
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> handleJwtVerification(JWTVerificationException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Invalid JWT", ex.getMessage()));
    }
    
    @ExceptionHandler(QuestionAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleQuestionAlreadyExists(QuestionAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Question duplicated", ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleQuestionAlreadyExists(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Bad credentials", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleQuestionAlreadyExists(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Bad argument", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleQuestionAlreadyExists2(MethodArgumentNotValidException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Argument not valid", "Algun parametro no cumple las validaciones"));
    }

    @ExceptionHandler(QuestionOwnershipException.class)
    public ResponseEntity<ErrorResponse> handleQuestionAlreadyExists2(QuestionOwnershipException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Question Ownership Exception", "Solo el creador puede marcar la respuesta como correcta"));
    }

    @ExceptionHandler(SelfVotingException.class)
    public ResponseEntity<ErrorResponse> handleSelfVotingException(SelfVotingException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Self Voting Exception", "No puedes votar tu misma respuesta"));
    }

    @ExceptionHandler(InvalidDoubleVoteException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDoubleVoteException(InvalidDoubleVoteException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Invalid Double Vote Exception", "No puedes votar dos veces una respuesta"));
    }

    @ExceptionHandler(ExceededTagsException.class)
    public ResponseEntity<ErrorResponse> handleExceededTagsException(ExceededTagsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Exceeded Tags Exception", "Maximo 5 tags por pregunta"));
    }

}
