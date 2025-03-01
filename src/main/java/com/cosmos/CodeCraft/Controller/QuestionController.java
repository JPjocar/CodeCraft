/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Dto.QuestionCreationDTO;
import com.cosmos.CodeCraft.Dto.QuestionDTO;
import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "question")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> show(@PathVariable("id") Long id){
        QuestionResponseDTO question = this.questionService.get(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(question);
    }
    
    
    //Create Question with answers
    @PostMapping
    public ResponseEntity<QuestionResponseDTO> create(@RequestBody @Valid QuestionCreationDTO questionCreationDTO){
        QuestionResponseDTO questionCreated = this.questionService.create(questionCreationDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(questionCreated);
    }
    
    //Create Question with tags
    @PostMapping("/create")
    public ResponseEntity<QuestionResponseDTO> create2(@RequestBody @Valid QuestionCreationDTO questionCreationDTO){
        QuestionResponseDTO questionCreated = this.questionService.create2(questionCreationDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(questionCreated);
    }
    
    //Delete question
    @DeleteMapping("/{question_id}")
    public ResponseEntity<String> delete(@PathVariable("question_id") Long id){
        String resul = this.questionService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(resul);
    }
    
    @PutMapping("/{question_id}")
    public ResponseEntity<QuestionResponseDTO> update(@RequestBody QuestionCreationDTO questionCreationDTO, @PathVariable("question_id") Long id){
        QuestionResponseDTO questionResponseDTO = this.questionService.update(questionCreationDTO, id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(questionResponseDTO);
    }
    
    
    
    
    //Create only question
    
    
    

}
