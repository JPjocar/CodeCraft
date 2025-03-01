/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Dto.CommentCreationDTO;
import com.cosmos.CodeCraft.Dto.CommentResponseDTO;
import com.cosmos.CodeCraft.Service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/comment")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @PostMapping("/question/{question_id}")
    public ResponseEntity<CommentResponseDTO> createForQuestion(@RequestBody @Valid CommentCreationDTO commentCreationDTO, @PathVariable("question_id") Long id){
        CommentResponseDTO commentResponseDTO = this.commentService.createForQuestion(commentCreationDTO, id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentResponseDTO);
    }
    
    @PostMapping("/answer/{answer_id}")
    public ResponseEntity<CommentResponseDTO> createForAnswer(@RequestBody @Valid CommentCreationDTO commentCreationDTO, @PathVariable("answer_id") Long id){
        CommentResponseDTO commentResponseDTO = this.commentService.createForAnswer(commentCreationDTO, id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentResponseDTO);
    }
    
    
    //Edit comment
    @PutMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDTO> update(@RequestBody @Valid CommentCreationDTO commentCreationDTO, @PathVariable("comment_id") Long id){
        CommentResponseDTO commentResponseDTO = this.commentService.update(commentCreationDTO, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentResponseDTO);
    }
    
    
    //Delete comment
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<String> delete(@PathVariable("comment_id") Long id){
        String response = this.commentService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }
}
