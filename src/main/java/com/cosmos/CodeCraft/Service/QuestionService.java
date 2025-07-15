/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */ 
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.*;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Entity.TagEntity;
import com.cosmos.CodeCraft.Entity.UserEntity;
import com.cosmos.CodeCraft.Exception.InsufficientTagsException;
import com.cosmos.CodeCraft.Exception.QuestionAlreadyExistsException;
import com.cosmos.CodeCraft.Exception.ResourceNotFoundException;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import com.cosmos.CodeCraft.Repository.TagRepository;
import com.cosmos.CodeCraft.Repository.UserRepository;
import com.github.slugify.Slugify;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
   
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private Slugify slg;
    
    @Transactional(readOnly = true)
    public QuestionResponseDTO get(Long id){
        QuestionEntity questionEntity = this.questionRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Question", "id", id) );
        return mapToResponse(questionEntity);
    }
    
    @Transactional(readOnly = true)
    public Page<QuestionResponseDTO> getAll(Pageable pageable){
        Page<QuestionEntity> pageQuestion = this.questionRepository.findAll(pageable);
        Page<QuestionResponseDTO> pageQuestionResponse = pageQuestion.map((question) -> {
            return mapToResponse(question);
        });
        return pageQuestionResponse;
    }
    
    @Transactional(readOnly = true)
    public List<QuestionResponseDTO> getAll2(Pageable pageable){
        Page<QuestionEntity> pageQuestion = this.questionRepository.findAll(pageable);
        return pageQuestion.getContent().stream().map(questionEntity -> mapToResponse(questionEntity)).toList();
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
    
//    public QuestionResponseDTO create(QuestionCreationDTO questionCreationDTO){
//        ModelMapper modelMapper = new ModelMapper();
//        QuestionEntity questionEntity = modelMapper.map(questionCreationDTO, QuestionEntity.class);
//        this.questionRepository.save(questionEntity);
//        return modelMapper.map(questionEntity, QuestionResponseDTO.class);
//    }
    
    
    public QuestionResponseDTO create2(QuestionCreationDTO questionCreationDTO){
        Set<TagEntity> tags = this.tagRepository.findTagEntityByNameIn(questionCreationDTO.getTags()).stream().collect(Collectors.toSet());
        if(tags.isEmpty() || tags.size() > 5){
            //Excepcion cuando no hay suficiente tags
            throw new InsufficientTagsException(tags.size());
        }
        ModelMapper modelMapper = new ModelMapper();
        QuestionEntity questionEntity = modelMapper.map(questionCreationDTO, QuestionEntity.class);
        questionEntity.setTags(tags);
        this.questionRepository.save(questionEntity);
        return mapToResponse(questionEntity);
    }
    
    @Transactional
    public QuestionResponseDTO create(QuestionCreationDTO questionCreationDTO, String username){
        String slug = this.slg.slugify(questionCreationDTO.getTitle());
        this.slugExists(slug); 
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username).orElseThrow(() -> new ResourceNotFoundException("UserEntity", "username", username));
        Set<TagEntity> tags = new HashSet<>(this.tagRepository.findTagEntityByNameIn(questionCreationDTO.getTags()));
        if(tags.isEmpty() || tags.size() > 5){
            throw new InsufficientTagsException(tags.size());
        }
        ModelMapper modelMapper = new ModelMapper();
        QuestionEntity questionEntity = modelMapper.map(questionCreationDTO, QuestionEntity.class);
        questionEntity.setSlug(slug);
        questionEntity.setTags(tags);
        questionEntity.setUser(userEntity);
        this.questionRepository.save(questionEntity);
        return mapToResponse(questionEntity);
    }
    
    private void slugExists(String slug){
        boolean existsSlug = this.questionRepository.findQuestionEntityBySlug(slug).isPresent();
        if(existsSlug) throw new QuestionAlreadyExistsException();
    }
    
    public String delete(Long id){
        this.questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        this.questionRepository.deleteById(id);
        return "Question removed!";
    }
    
    //Actualizar
    @Transactional
    public QuestionResponseDTO update(QuestionCreationDTO questionCreationDTO, Long id){
        Set<TagEntity> tags = this.tagRepository.findTagEntityByNameIn(questionCreationDTO.getTags()).stream().collect(Collectors.toSet());
        if(tags.isEmpty() || tags.size() > 5){ //Verificar que haya 5 tags, igual para el metodo de crear
            throw new InsufficientTagsException(tags.size());
        }
        String slug = slg.slugify(questionCreationDTO.getTitle());
        Optional<QuestionEntity> questionExists = this.questionRepository.findQuestionEntityBySlug(slug);
        QuestionEntity questionEntity = this.questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        if( questionExists.isPresent() && !Objects.equals(questionExists.get().getId(), id) ){
            throw new IllegalArgumentException("Slug already exists");
        }
        questionEntity.setTitle(questionCreationDTO.getTitle());
        questionEntity.setContent(questionCreationDTO.getContent());
        questionEntity.setTags(tags);
        questionEntity.setSlug(slug);
        this.questionRepository.save(questionEntity);
        return mapToResponse(questionEntity);
    }
    
    
    private QuestionResponseDTO mapToResponse(QuestionEntity questionEntity){
        ModelMapper modelMapper = new ModelMapper();
        
        QuestionResponseDTO questionResponseDTO = modelMapper.map(questionEntity, QuestionResponseDTO.class);
        
        //Asignar tags response
        questionResponseDTO.setTags(questionEntity.getTags()
                .stream()
                .map(tag -> modelMapper.map(tag, TagResponseDTO.class))
                .collect(Collectors.toSet()));
        
        //Asignar comments response
        questionResponseDTO.setComments(questionEntity.getComments()
                .stream()
                .map(comment -> modelMapper.map(comment, CommentResponseDTO.class))
                .toList());
        
        //Asignar answers response
        questionResponseDTO.setAnswers(questionEntity.getAnswers()
                .stream()
                .map(answer -> {
                    AnswerResponseDTO answerResponseDTO = modelMapper.map(answer, AnswerResponseDTO.class);
                    answerResponseDTO.setComments(answer.getComments()
                            .stream()
                            .map(comment -> modelMapper.map(comment, CommentResponseDTO.class) )
                            .toList());
                    return answerResponseDTO;
                }).toList());

        if(questionEntity.getUser() != null){
            UserResponseDTO userResponseDTO = modelMapper.map(questionEntity.getUser(), UserResponseDTO.class);
            questionResponseDTO.setUser(userResponseDTO);
        }


        return questionResponseDTO;
    }
    

    
}
