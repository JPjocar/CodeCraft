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
import java.util.Optional;
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
    
    public AnswerResponseDTO update(AnswerCreationDTO answerCreationDTO, Long answer_id){
        AnswerEntity answerEntity = this.answerRepository.findById(answer_id).orElseThrow();
        //Actualizar contenido de la respuesta
        answerEntity.setContent(answerCreationDTO.getContent());
        this.answerRepository.save(answerEntity);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(answerEntity, AnswerResponseDTO.class);
    }
    
    public String delete(Long id){
        Optional<AnswerEntity> answerOptional = this.answerRepository.findById(id);
        if(answerOptional.isEmpty()){
            return "La respuesta con id: "+id+" no existe";
        }
        this.answerRepository.deleteById(id);
        return "Se ha eliminado la respuesta";
    }
    
}
