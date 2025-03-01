/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.CommentCreationDTO;
import com.cosmos.CodeCraft.Dto.CommentResponseDTO;
import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.CommentEntity;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Repository.AnswerRepository;
import com.cosmos.CodeCraft.Repository.CommentRepository;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
    public CommentResponseDTO createForQuestion(CommentCreationDTO commentCreationDTO, Long id){
        boolean existQuestion = this.questionRepository.existsById(id);
        if(!existQuestion){
            throw new IllegalArgumentException("No existe el ID en preguntas");
        }
        ModelMapper modelMapper = new ModelMapper();
        CommentEntity commentEntity = modelMapper.map(commentCreationDTO, CommentEntity.class);
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(id);
        commentEntity.setQuestionEntity(questionEntity);
        commentEntity.setPost_type("question");
        
        this.commentRepository.save(commentEntity);
        
        return modelMapper.map(commentEntity, CommentResponseDTO.class);
    }
    
    
    
    public CommentResponseDTO createForAnswer(CommentCreationDTO commentCreationDTO, Long id){
        boolean existAnswer = this.answerRepository.existsById(id);
        if(!existAnswer){
            throw new IllegalArgumentException("No existe el ID en preguntas");
        }
        ModelMapper modelMapper = new ModelMapper();
        CommentEntity commentEntity = modelMapper.map(commentCreationDTO, CommentEntity.class);
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setId(id);
        commentEntity.setAnswerEntity(answerEntity);
        commentEntity.setPost_type("answer");
        
        this.commentRepository.save(commentEntity);
        
        return modelMapper.map(commentEntity, CommentResponseDTO.class);
    }
    
    
    public CommentResponseDTO update(CommentCreationDTO commentCreationDTO, Long comment_id){
        CommentEntity commentEntity = this.commentRepository.findById(comment_id).orElseThrow();
        //Actualizar contenido del comentario
        commentEntity.setContent(commentCreationDTO.getContent());
        this.commentRepository.save(commentEntity);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(commentEntity, CommentResponseDTO.class);
    }
    
    
    public String delete(Long id){
        Optional<CommentEntity> commentOptional = this.commentRepository.findById(id);
        if(commentOptional.isEmpty()){
            return "El id del comentario no existe";
        }
        this.commentRepository.deleteById(id);
        return "El comentario se ha eliminado";
    }
}
