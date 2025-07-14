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
import com.cosmos.CodeCraft.Exception.ResourceNotFoundException;
import com.cosmos.CodeCraft.Repository.AnswerRepository;
import com.cosmos.CodeCraft.Repository.CommentRepository;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
//    public CommentResponseDTO createForQuestion(CommentCreationDTO commentCreationDTO, Long id){
//        boolean existQuestion = this.questionRepository.existsById(id);
//        if(!existQuestion){
//            throw new IllegalArgumentException("No existe el ID en preguntas");
//        }
//        ModelMapper modelMapper = new ModelMapper();
//        CommentEntity commentEntity = modelMapper.map(commentCreationDTO, CommentEntity.class);
//        QuestionEntity questionEntity = new QuestionEntity();
//        questionEntity.setId(id);
//        commentEntity.setQuestionEntity(questionEntity);
//        commentEntity.setPost_type("question");
//        
//        this.commentRepository.save(commentEntity);
//        
//        return modelMapper.map(commentEntity, CommentResponseDTO.class);
//    }
    
    public CommentResponseDTO createForQuestion(CommentCreationDTO commentCreationDTO, Long question_id){
        QuestionEntity questionEntity = this.questionRepository.findById(question_id)
                .orElseThrow( () -> new ResourceNotFoundException("Question", "id", question_id) );
        ModelMapper modelMapper = new ModelMapper();
        CommentEntity commentEntity = modelMapper.map(commentCreationDTO, CommentEntity.class);
        commentEntity.setQuestionEntity(questionEntity);
        commentEntity.setPost_type("question");
        this.commentRepository.save(commentEntity);        
        return mapToResponse(commentEntity);
    }
    
    
    public CommentResponseDTO createForAnswer(CommentCreationDTO commentCreationDTO, Long answer_id){
        AnswerEntity answerEntity = this.answerRepository.findById(answer_id)
                .orElseThrow( () -> new ResourceNotFoundException("Answer", "id", answer_id) );
        ModelMapper modelMapper = new ModelMapper();
        CommentEntity commentEntity = modelMapper.map(commentCreationDTO, CommentEntity.class);
        commentEntity.setAnswerEntity(answerEntity);
        commentEntity.setPost_type("answer");
        this.commentRepository.save(commentEntity);
        return mapToResponse(commentEntity);
    }
    
    
    public CommentResponseDTO update(CommentCreationDTO commentCreationDTO, Long comment_id){
        CommentEntity commentEntity = this.commentRepository.findById(comment_id)
                .orElseThrow( () -> new ResourceNotFoundException("Comment", "id", comment_id) );
        //Actualizar contenido del comentario
        commentEntity.setContent(commentCreationDTO.getContent());
        this.commentRepository.save(commentEntity);
        return mapToResponse(commentEntity);
    }
    
    
    public String delete(Long id){
        this.commentRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Comment", "id", id) );
        this.commentRepository.deleteById(id);
        return "Comment removed!";
    }
    
    private CommentResponseDTO mapToResponse(CommentEntity commentEntity){
        ModelMapper modelMapper = new ModelMapper();
        CommentResponseDTO commentResponseDTO = modelMapper.map(commentEntity, CommentResponseDTO.class);
        return commentResponseDTO;
    }
}
