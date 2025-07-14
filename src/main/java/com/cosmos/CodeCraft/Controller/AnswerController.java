/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Dto.AnswerCreationDTO;
import com.cosmos.CodeCraft.Dto.AnswerResponseDTO;
import com.cosmos.CodeCraft.Dto.CorrectAnswerDTO;
import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Service.AnswerService;
import com.cosmos.CodeCraft.Service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "answer")
public class AnswerController {
    
    @Autowired
    private AnswerService answerService;
    
    @PostMapping("/is-correct")
    public ResponseEntity<String> isCorrect(@RequestBody CorrectAnswerDTO correctAnswerDTO, @AuthenticationPrincipal String username){
        String resul = this.answerService.isCorrect(correctAnswerDTO, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resul);
    }
    
    //Create answers for a question
    @PostMapping("/{question_id}")
    public AnswerResponseDTO create(@RequestBody @Valid AnswerCreationDTO answerCreationDTO, @PathVariable("question_id") Long question_id){
        AnswerResponseDTO answerResponseDTO = this.answerService.create(answerCreationDTO, question_id);
        return answerResponseDTO;
    }
    
    //Update answer for a question
    @PutMapping("/{answer_id}")
    public AnswerResponseDTO update(@RequestBody @Valid AnswerCreationDTO answerCreationDTO, @PathVariable("answer_id") Long id){
        AnswerResponseDTO answerResponseDTO = this.answerService.update(answerCreationDTO, id);
        return answerResponseDTO;
    }
    
    
    @DeleteMapping("/{answer_id}")
    public ResponseEntity<String> delete(@PathVariable("answer_id") Long id){
        String response = this.answerService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }
    
//    {
//        "question_id", "answer_id", "username";
//    }
    
    
}
