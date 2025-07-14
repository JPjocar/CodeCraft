/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.DataProvider;
import com.cosmos.CodeCraft.Dto.CommentCreationDTO;
import com.cosmos.CodeCraft.Dto.CommentResponseDTO;
import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Exception.ErrorResponse;
import com.cosmos.CodeCraft.Exception.ResourceNotFoundException;
import com.cosmos.CodeCraft.Service.CommentService;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 *
 * @author Cosmos
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;


    // Crear una pregunta ( Solo usuarios con permisos WRITE_POST )
    @Test
    public void createForQuestionTest() throws Exception {

        int question_id = 1;
        String content = "comentario para la pregunta numero 1";
        String body = "{\"content\":\"" + content + "\"}";

        String token = obtenerToken("admin", "12345");

        MvcResult result = mockMvc.perform(post("/comment/question/"+question_id)
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CommentResponseDTO comment = mapper.readValue(json, CommentResponseDTO.class);

        Assertions.assertEquals(content, comment.getContent());

    }

    @Test
    public void createForQuestionTestFail() throws Exception {

        int question_id = 16;
        String content = "comentario para la pregunta numero 1";
        String body = "{\"content\":\"" + content + "\"}";

        String token = obtenerToken("admin", "12345");

        MvcResult result = mockMvc.perform(post("/comment/question/"+question_id)
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse err = mapper.readValue(json, ErrorResponse.class);

        Assertions.assertEquals("Resource not found", err.getErrorCode());
        Assertions.assertEquals("Question not found with id: '16'", err.getMessage());
    }


    @Test
    public void createForAnswerTest() throws Exception {

        int question_id = 1;
        String content = "comentario para la respuesta numero 1";
        String body = "{\"content\":\"" + content + "\"}";

        String token = obtenerToken("admin", "12345");

        MvcResult result = mockMvc.perform(post("/comment/answer/"+question_id)
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CommentResponseDTO comment = mapper.readValue(json, CommentResponseDTO.class);

        Assertions.assertEquals(content, comment.getContent());

    }
    @Test
    public void createForAnswerTestFail() throws Exception {

        int answer_id = 16;
        String content = "comentario para la respuesta numero 16";
        String body = "{\"content\":\"" + content + "\"}";

        String token = obtenerToken("admin", "12345");

        MvcResult result = mockMvc.perform(post("/comment/answer/"+answer_id)
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse err = mapper.readValue(json, ErrorResponse.class);

        Assertions.assertEquals("Resource not found", err.getErrorCode());
        Assertions.assertEquals("Answer not found with id: '16'", err.getMessage());
    }

    private String obtenerToken(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        String response = mockMvc.perform(post("/auth/log-in")
                        .contentType("application/json")
                        .content(body))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.split("\"token\":\"")[1].split("\"")[0];
    }








    /*
    @Mock
    private CommentService commentService;
    
    @InjectMocks
    private CommentController commentController;
    
    @Test
    public void testCreateForQuestion(){
        //Give
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO("comentario de pregunta");
        Long question_id = 5L;
        
        //When
        when(commentService.createForQuestion(any(CommentCreationDTO.class), anyLong()))
                .thenReturn(DataProvider.getCreatedComment());
        CommentResponseDTO commentResponseDTO = commentController.createForQuestion(commentCreationDTO, question_id);
        
        //Then
        assertEquals("comentario de algo", commentResponseDTO.getContent());
        assertEquals(1l, commentResponseDTO.getId());
        assertEquals("comentario", commentResponseDTO.getPost_type());
        assertEquals(CommentResponseDTO.class, commentResponseDTO.getClass());
    }
    
    
    
    @Test
    public void testCreateForQuestionFail(){
        //Give
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO("comentario de pregunta");
        Long question_id = 5L;
        
        //When
        when(commentService.createForQuestion(any(CommentCreationDTO.class), anyLong()))
                .thenThrow( new ResourceNotFoundException("Question", "id", question_id) );
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> commentController.createForQuestion(commentCreationDTO, question_id));
        
        //Then
        assertEquals("Question not found with id: '5'", exception.getMessage());
    }
    
    @Test
    public void testCreateForAnswer(){
        //Give
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO("comentario de pregunta");
        Long answer_id = 5L;
        
        //When
        when(commentService.createForAnswer(any(CommentCreationDTO.class), anyLong()))
                .thenReturn(DataProvider.getCreatedComment());
        CommentResponseDTO commentResponseDTO = commentController.createForAnswer(commentCreationDTO, answer_id);
        
        //Then
        assertEquals("comentario de algo", commentResponseDTO.getContent());
        assertEquals(1l, commentResponseDTO.getId());
        assertEquals("comentario", commentResponseDTO.getPost_type());
        assertEquals(CommentResponseDTO.class, commentResponseDTO.getClass());
    }
    
    @Test
    public void testCreateForAnswerFail(){
        //Give
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO("comentario de pregunta");
        Long answer_id = 5L;
        
        //When
        when(commentService.createForAnswer(any(CommentCreationDTO.class), anyLong()))
                .thenThrow(new ResourceNotFoundException("Answer", "id", answer_id));
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> commentController.createForAnswer(commentCreationDTO, answer_id));
        
        //Then
        assertEquals("Answer not found with id: '5'", exception.getMessage());

    }
    */

}
