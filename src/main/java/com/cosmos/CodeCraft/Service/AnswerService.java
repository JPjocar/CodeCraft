/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.*;
import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Entity.UserEntity;
import com.cosmos.CodeCraft.Entity.VoteEntity;
import com.cosmos.CodeCraft.Exception.InvalidDoubleVoteException;
import com.cosmos.CodeCraft.Exception.QuestionOwnershipException;
import com.cosmos.CodeCraft.Exception.ResourceNotFoundException;
import com.cosmos.CodeCraft.Exception.SelfVotingException;
import com.cosmos.CodeCraft.Repository.AnswerRepository;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cosmos.CodeCraft.Repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;



    public AnswerResponseDTO isSolution(Long id, String username){
        AnswerEntity answerEntity = this.answerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("answer", "id", id)
        );
        UserEntity userEntity = this.userDetailsServiceImpl.findByUsername(username);
        QuestionEntity questionEntity = answerEntity.getQuestion();
        //La pregunta es del dueño?
        if(!questionEntity.getUser().getId().equals(userEntity.getId())){
            throw new QuestionOwnershipException("Only question owner can mark as correct");
        }
        List<AnswerEntity> answerCorrect = this.answerRepository.findByQuestionAndIsCorrectTrueAndIdNot(questionEntity, answerEntity.getId());
        if(!answerCorrect.isEmpty()){
            answerCorrect.getFirst().set_correct(false);
            this.answerRepository.save(answerCorrect.getFirst());
        }
        answerEntity.set_correct(!answerEntity.is_correct());
        AnswerEntity answer = this.answerRepository.save(answerEntity);
        return mapToResponse(answer);
    }

    public VoteResponseDTO vote(Long answer_id, boolean vote, String username){
        AnswerEntity answerEntity = this.answerRepository.findById(answer_id).orElseThrow();
        UserEntity userEntity = this.userDetailsServiceImpl.findByUsername(username);
        UserEntity userOwner = answerEntity.getUser();
        VoteEntity voteUser;
        if(userOwner.getId().equals(userEntity.getId())){
            throw new SelfVotingException("User don't self voting");
        }
        Optional<VoteEntity> voteEntity = this.voteRepository.findByUserEntityAndAnswerEntity(userEntity, answerEntity);
        if(voteEntity.isPresent()) {
            if (voteEntity.get().isUtil() == vote){
                throw new InvalidDoubleVoteException("User don't vote two o more times");
            }else {
                voteEntity.get().setUtil(!voteEntity.get().isUtil());
                voteUser = voteEntity.get();
            }
        }else{
            voteUser = new VoteEntity(null, answerEntity, userEntity, vote);
        }
        answerEntity.setScore(answerEntity.getScore()+(vote ? 1 : -1));
        userOwner.setReputation(userOwner.getReputation()+2);
        this.answerRepository.save(answerEntity);
        this.userDetailsServiceImpl.save(userOwner);
        this.voteRepository.save(voteUser);
        return mapToResponseVoteDTO(voteUser);
    }
    /*
    public AnswerResponseDTO vote(Long answer_id, boolean vote, String username){
        AnswerEntity answerEntity = this.answerRepository.findById(answer_id).orElseThrow();
        UserEntity userEntity = this.userDetailsServiceImpl.findByUsername(username);
        UserEntity userOwner = answerEntity.getUser();
        VoteEntity voteUser;
        int point = vote ? 1 : -1;
        if(userOwner.getId().equals(userEntity.getId())){
            throw new SelfVotingException("User don't self voting");
        }
        Optional<VoteEntity> voteEntity = this.voteRepository.findByUserEntityAndAnswerEntity(userEntity, answerEntity);
        if(voteEntity.isPresent()) {
            if (voteEntity.get().getPoint() == point){
                throw new InvalidDoubleVoteException("User don't vote two o more times");
            }else {
                voteEntity.get().setPoint(voteEntity.get().getPoint()+point);
                voteUser = voteEntity.get();
            }
        }else{
            voteUser = new VoteEntity(null, answerEntity, userEntity, point);
        }
        answerEntity.setScore(answerEntity.getScore()+point);
        userOwner.setReputation(userOwner.getReputation()+2);
        this.answerRepository.save(answerEntity);
        this.userDetailsServiceImpl.save(userOwner);
        this.voteRepository.save(voteUser);
        return mapToResponse(answerEntity);
    }
*/


    //Create a answer
//    public AnswerResponseDTO create(AnswerCreationDTO answerCreationDTO, Long question_id){
//        boolean existsQuestion = this.questionRepository.existsById(question_id);
//        if(!existsQuestion){
//            throw new ResourceNotFoundException("Question", "id", question_id);
//        }
//        QuestionEntity questionEntity = new QuestionEntity();
//        questionEntity.setId(question_id);
//        ModelMapper modelMapper = new ModelMapper();
//        AnswerEntity answerEntity = modelMapper.map(answerCreationDTO, AnswerEntity.class);
//        answerEntity.setQuestion(questionEntity);
//        this.answerRepository.save(answerEntity);
//        return modelMapper.map(answerEntity, AnswerResponseDTO.class);
//    }
    
    public AnswerResponseDTO create(AnswerCreationDTO answerCreationDTO, Long question_id, String username){
        QuestionEntity questionEntity = this.questionRepository.findById(question_id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", question_id));

        UserEntity userEntity = this.userDetailsServiceImpl.findByUsername(username);

        ModelMapper modelMapper = new ModelMapper();
        AnswerEntity answerEntity = modelMapper.map(answerCreationDTO, AnswerEntity.class);
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userEntity);
        this.answerRepository.save(answerEntity);
        return mapToResponse(answerEntity);
    }
    
    public AnswerResponseDTO update(AnswerCreationDTO answerCreationDTO, Long answer_id){
        AnswerEntity answerEntity = this.answerRepository.findById(answer_id)
                .orElseThrow( () -> new ResourceNotFoundException("Answer", "id", answer_id) );
        //Actualizar contenido de la respuesta
        answerEntity.setContent(answerCreationDTO.getContent());
        this.answerRepository.save(answerEntity);
        return mapToResponse(answerEntity);
    }
    
    public String delete(Long id){
        this.answerRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Answer", "id", id) );
        this.answerRepository.deleteById(id);
        return "Answer removed";
    }
  
    private AnswerResponseDTO mapToResponse(AnswerEntity answerEntity){
        ModelMapper modelMapper = new ModelMapper();
        AnswerResponseDTO answerResponseDTO = modelMapper.map(answerEntity, AnswerResponseDTO.class);
        answerResponseDTO.setComments(answerEntity.getComments()
                .stream()
                .map(comment -> modelMapper.map(comment, CommentResponseDTO.class))
                .toList());
        if(answerEntity.getUser() != null){
            UserResponseDTO userResponseDTO = modelMapper.map(answerEntity.getUser(), UserResponseDTO.class);
            answerResponseDTO.setUser(userResponseDTO);
        }
        return answerResponseDTO;
    }

    private VoteResponseDTO mapToResponseVoteDTO(VoteEntity voteEntity){
        ModelMapper modelMapper = new ModelMapper();
        VoteResponseDTO voteResponseDTO = modelMapper.map(voteEntity, VoteResponseDTO.class);
        voteResponseDTO.setUserResponseDTO(modelMapper.map(voteEntity.getUserEntity(), UserResponseDTO.class));
        voteResponseDTO.setAnswerResponseDTO(modelMapper.map(voteEntity.getAnswerEntity(), AnswerResponseDTO.class));
        return voteResponseDTO;
    }


    
    @Transactional
    public String isCorrect(CorrectAnswerDTO correctAnswerDTO, String username){
        QuestionEntity question = this.questionRepository.findByIdAndOwner(correctAnswerDTO.question_id(), username)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", username));

        List<AnswerEntity> answers = question.getAnswers();
        boolean existsCorrect = answers.stream()
                .anyMatch(answer -> answer.is_correct() == true);
        if(existsCorrect){
            return "Ya existe una respuesta correcta";
        }
        boolean existAnswer = answers.stream()
                .anyMatch(answer -> Objects.equals(answer.getId(), correctAnswerDTO.answer_id()));
        if(!existAnswer){
            return "No existe la respuesta";
        }
        answers.forEach(answer -> {
            if(Objects.equals(answer.getId(), correctAnswerDTO.answer_id())){
                System.out.println("Answer id: "+answer.getId()+" y correct: "+correctAnswerDTO.answer_id());
                answer.set_correct(true);
            }
        });
        this.answerRepository.saveAll(answers);
        return "La respuesta ha sido calificada como correcta";
    }
    
}
