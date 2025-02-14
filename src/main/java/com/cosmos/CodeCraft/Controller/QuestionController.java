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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "question")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> show(@PathVariable("id") Long id){
        QuestionDTO question = this.questionService.get(id);
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
    
    
    //Create only question
    
    
    

}
