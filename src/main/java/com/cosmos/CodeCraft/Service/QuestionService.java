/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */ 
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.AnswerBasicDTO;
import com.cosmos.CodeCraft.Dto.CommentResponseDTO;
import com.cosmos.CodeCraft.Dto.QuestionCreationDTO;
import com.cosmos.CodeCraft.Dto.QuestionDTO;
import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Dto.TagResponseDTO;
import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Entity.TagEntity;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import com.cosmos.CodeCraft.Repository.TagRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
   
    @Autowired
    private TagRepository tagRepository;
    
    public QuestionResponseDTO get(Long id){
        Optional<QuestionEntity> question = this.questionRepository.findById(id);
        if(question.isEmpty()){
            throw new RuntimeException();
        }
        QuestionEntity questionEntity = question.get();
        ModelMapper modelMapper = new ModelMapper();
        QuestionResponseDTO questionDTO = modelMapper.map(questionEntity, QuestionResponseDTO.class);
        questionDTO.setAnswers(questionEntity.getAnswers().stream().map(answer -> {
            AnswerBasicDTO answerBasicDTO = modelMapper.map(answer, AnswerBasicDTO.class);
            answerBasicDTO.setComments(answer.getComments().stream().map(comment -> {
                return modelMapper.map(comment, CommentResponseDTO.class);
            }).toList());
            return answerBasicDTO;
        }).toList());
        
        questionDTO.setComments(questionEntity.getComments().stream().map(comment -> {
            return modelMapper.map(comment, CommentResponseDTO.class);
        }).toList());
        questionDTO.setTags(questionEntity.getTags().stream().map(tag -> {
          return modelMapper.map(tag, TagResponseDTO.class);
        }).collect(Collectors.toSet()));
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
    
    
    
    public QuestionResponseDTO create2(QuestionCreationDTO questionCreationDTO){
        Set<TagEntity> tags = this.tagRepository.findTagEntityByNameIn(questionCreationDTO.getTags()).stream().collect(Collectors.toSet());
        if(tags.isEmpty()){
            throw new IllegalArgumentException("No hay tags");
        }
        ModelMapper modelMapper = new ModelMapper();
        QuestionEntity questionEntity = modelMapper.map(questionCreationDTO, QuestionEntity.class);
        questionEntity.setTags(tags);
        this.questionRepository.save(questionEntity);
        return this.get(questionEntity.getId());
    }
    
    public String delete(Long id){
        Optional<QuestionEntity> optional = this.questionRepository.findById(id);
        if(optional.isPresent()){
            this.questionRepository.deleteById(id);
            return "Pregunta con ID: "+id+" ha sido eliminada";
        }
        return "Pregunta con ID: "+id+" no existe";
        
    }
    
    public QuestionResponseDTO update(QuestionCreationDTO questionCreationDTO, Long id){
        Set<TagEntity> tags = this.tagRepository.findTagEntityByNameIn(questionCreationDTO.getTags()).stream().collect(Collectors.toSet());
        if(tags.isEmpty() || tags.size() > 5){ //Verificar que haya 5 tags, igual para el metodo de crear
            throw new IllegalArgumentException("No hay tags o tags son mayores a 5");
        }
        Optional<QuestionEntity> questionOptional = this.questionRepository.findById(id);
        if(questionOptional.isEmpty()){
            throw new IllegalArgumentException("No existe pregunta con el id");
        }
        QuestionEntity questionEntity = questionOptional.get();
        questionEntity.setTitle(questionCreationDTO.getTitle());
        questionEntity.setContent(questionCreationDTO.getContent());
        questionEntity.setTags(tags);
        this.questionRepository.save(questionEntity);
        return this.get(questionEntity.getId());
    }
    
}
