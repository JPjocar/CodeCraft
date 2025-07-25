/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreationDTO {
    @NotBlank
    @NotNull
    @Size(min = 10)
    private String title;
    
    @NotBlank
    @NotNull
    @Size(min = 23)
    private String content;
    

    private Set<String> tags;
    
//    private List<AnswerCreationDTO> answers;
}
