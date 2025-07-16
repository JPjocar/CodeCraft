package com.cosmos.CodeCraft.Dto;

import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponseDTO {
    private Long id;

    private AnswerResponseDTO answerResponseDTO;

    private UserResponseDTO userResponseDTO;


    private boolean util;
}
