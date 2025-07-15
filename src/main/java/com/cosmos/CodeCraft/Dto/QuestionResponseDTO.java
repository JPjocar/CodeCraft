/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Dto;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseDTO {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private Set<TagResponseDTO> tags;
    private List<CommentResponseDTO> comments;
    private List<AnswerResponseDTO> answers;
    private UserResponseDTO user;
}
