/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */ 
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.AnswerBasicDTO;
import com.cosmos.CodeCraft.Dto.QuestionCreationDTO;
import com.cosmos.CodeCraft.Dto.QuestionDTO;
import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    public QuestionDTO get(Long id){
        Optional<QuestionEntity> question = this.questionRepository.findById(id);
        if(question.isEmpty()){
            throw new RuntimeException();
        }
        QuestionEntity questionEntity = question.get();
        ModelMapper modelMapper = new ModelMapper();
        QuestionDTO questionDTO = modelMapper.map(questionEntity, QuestionDTO.class);
        questionDTO.setAnswers(questionEntity.getAnswers().stream().map(answer -> {
            return modelMapper.map(answer, AnswerBasicDTO.class);
        }).toList());
        
        return questionDTO;
    }
    
//    public QuestionResponseDTO create(QuestionCreationDTO questionCreationDTO){
//        ModelMapper modelMapper = new ModelMapper();
//        QuestionEntity questionEntity = modelMapper.map(questionCreationDTO, QuestionEntity.class);
//        questionEntity.setAnswers(questionCreationDTO.getAnswers().stream().map(answer -> {
//            return modelMapper.map(answer, AnswerEntity.class);
//        }).toList());
//        this.questionRepository.save(questionEntity);
//        return modelMapper.map(questionEntity, QuestionResponseDTO.class);
//    }
    
    public QuestionResponseDTO create(QuestionCreationDTO questionCreationDTO){
        ModelMapper modelMapper = new ModelMapper();
        QuestionEntity questionEntity = modelMapper.map(questionCreationDTO, QuestionEntity.class);
        this.questionRepository.save(questionEntity);
        return modelMapper.map(questionEntity, QuestionResponseDTO.class);
    }
    
    
}
