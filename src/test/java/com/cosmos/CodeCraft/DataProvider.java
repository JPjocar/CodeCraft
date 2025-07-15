/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft;

import com.cosmos.CodeCraft.Dto.AnswerResponseDTO;
import com.cosmos.CodeCraft.Dto.AuthResponse;
import com.cosmos.CodeCraft.Dto.CommentResponseDTO;
import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Dto.TagResponseDTO;
import com.cosmos.CodeCraft.Entity.CommentEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Cosmos
 */
public class DataProvider {


    public static AuthResponse getLoguedUser() {
        return new AuthResponse("user", "message", "12345678", true);
    }

    public static AuthResponse getRegisterUser() {
        return new AuthResponse("user", "message", "12345678", true);
    }

    public static QuestionResponseDTO getQuestion() {
        return new QuestionResponseDTO(
                1l,
                "titulo question",
                "titulo-question",
                "content",
                Set.of(
                        new TagResponseDTO(1l, "java", ""),
                        new TagResponseDTO(2l, "python", "")
                ),
                List.of(
                        new CommentResponseDTO(1l, "comment 1", "comment"),
                        new CommentResponseDTO(2l, "comment 2", "comment")
                ),
                List.of(
                        new AnswerResponseDTO(1l, "respuesta 1", 0, true, null),
                        new AnswerResponseDTO(2l, "respuesta 2", 0, true, null)
                ));
    }

    public static QuestionResponseDTO getUpdatedQuestion() {
        return new QuestionResponseDTO(
                1l,
                "titulo updated",
                "titulo-updated",
                "content updated",
                Set.of(
                        new TagResponseDTO(1l, "java", ""),
                        new TagResponseDTO(2l, "python", "")
                ),
                List.of(
                        new CommentResponseDTO(1l, "comment 1", "comment"),
                        new CommentResponseDTO(2l, "comment 2", "comment")
                ),
                List.of(
                        new AnswerResponseDTO(1l, "respuesta 1", 0, true, null),
                        new AnswerResponseDTO(2l, "respuesta 2", 0, true, null)
                ));
    }

    public static AnswerResponseDTO getCreatedAnswer() {
        return new AnswerResponseDTO(1L, "respuesta n1", 0, true,
                List.of(
                        new CommentResponseDTO(1l, "comment 1", "comment"),
                        new CommentResponseDTO(2l, "comment 2", "comment")
                ));
    }

    public static AnswerResponseDTO getUpdatedAnswer() {
        return new AnswerResponseDTO(1L, "respuesta n1", 0, true,
                List.of(
                        new CommentResponseDTO(1l, "comment 1", "comment"),
                        new CommentResponseDTO(2l, "comment 2", "comment")
                ));
    }
    
    public static CommentResponseDTO getCreatedComment(){
        return new CommentResponseDTO(1l, "comentario de algo", "comentario"); 
    }
}
