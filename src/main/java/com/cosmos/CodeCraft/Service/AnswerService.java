/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.AnswerCreationDTO;
import com.cosmos.CodeCraft.Dto.AnswerResponseDTO;
import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Repository.AnswerRepository;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class AnswerService {
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    public AnswerResponseDTO create(AnswerCreationDTO answerCreationDTO, Long question_id){
        boolean existsQuestion = this.questionRepository.existsById(question_id);
        if(!existsQuestion){
            throw new IllegalArgumentException("question_id NOT FOUND");
        }
        
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(question_id);
        
        ModelMapper modelMapper = new ModelMapper();
        AnswerEntity answerEntity = modelMapper.map(answerCreationDTO, AnswerEntity.class);
        answerEntity.setQuestion(questionEntity);
        
        this.answerRepository.save(answerEntity);
        return modelMapper.map(answerEntity, AnswerResponseDTO.class);
    }
    
}
