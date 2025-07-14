/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.DataProvider;
import com.cosmos.CodeCraft.Dto.AnswerCreationDTO;
import com.cosmos.CodeCraft.Dto.AnswerResponseDTO;
import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Exception.ErrorResponse;
import com.cosmos.CodeCraft.Exception.ResourceNotFoundException;
import com.cosmos.CodeCraft.Service.AnswerService;
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
public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createTest() throws Exception {

        String content = "Respuesta para la pregunta numero 1 con imagenes";
        String token = obtenerToken("admin", "12345");
        int question_id = 1;
        String body = "{\"content\":\"" + content + "\"}";

        MvcResult result = mockMvc.perform(post("/answer/"+question_id)
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        AnswerResponseDTO question = mapper.readValue(json, AnswerResponseDTO.class);

        Assertions.assertEquals(content, question.getContent());
        Assertions.assertEquals(0, question.getScore());
        Assertions.assertFalse(question.is_correct());
        Assertions.assertTrue(question.getComments().isEmpty());
    }


    @Test
    public void createTestFail() throws Exception {

        String content = "Respuesta para la pregunta numero 1 con imagenes";
        String token = obtenerToken("admin", "12345");
        int question_id = 19;
        String body = "{\"content\":\"" + content + "\"}";

        MvcResult result = mockMvc.perform(post("/answer/"+question_id)
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse error = mapper.readValue(json, ErrorResponse.class);

        Assertions.assertEquals("Resource not found", error.getErrorCode());
        Assertions.assertEquals("Question not found with id: '19'", error.getMessage());

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
    private AnswerService answerService;
    
    @InjectMocks
    private AnswerController answerController;
    
    
    @Test
    public void testCreate(){
        //Give
        AnswerCreationDTO answerCreationDTO = new AnswerCreationDTO("respuesta a pregunta");
        Long question_id = 3L;
        
        //When
        when(answerService.create( any(AnswerCreationDTO.class) , anyLong() ))
                .thenReturn( DataProvider.getCreatedAnswer() );
        AnswerResponseDTO answerResponseDTO = this.answerController.create(answerCreationDTO, question_id);
        
        //Then
        assertEquals("respuesta n1", answerResponseDTO.getContent());
        assertEquals(0, answerResponseDTO.getScore());
        assertFalse(answerResponseDTO.getComments().isEmpty());
        
    }
    
    
    @Test
    public void testCreateFail(){
        //Give
        AnswerCreationDTO answerCreationDTO = new AnswerCreationDTO("respuesta a pregunta");
        Long question_id = 3L;
        
        //When
        when(answerService.create( any(AnswerCreationDTO.class) , anyLong() ))
                .thenThrow(new ResourceNotFoundException("Question", "id", question_id));
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> answerController.create(answerCreationDTO, question_id)
        );      
        
        //Then
        assertEquals("Question not found with id: '3'", exception.getMessage());
    }
    
    @Test
    public void testUpdate(){
        //Give
        AnswerCreationDTO answerCreationDTO = new AnswerCreationDTO("respuesta a pregunta");
        Long answer_id = 3L;
        //When
        when( answerService.update( any(AnswerCreationDTO.class) , anyLong()) )
                .thenReturn( DataProvider.getUpdatedAnswer() );
        AnswerResponseDTO answerResponseDTO = this.answerController.update(answerCreationDTO, answer_id);
        //Then
        assertEquals("respuesta n1", answerResponseDTO.getContent());
        assertEquals(0, answerResponseDTO.getScore());
        assertFalse(answerResponseDTO.getComments().isEmpty());
    }
    
    @Test
    public void testUpdateFail(){
        //Give
        AnswerCreationDTO answerCreationDTO = new AnswerCreationDTO("respuesta a pregunta");
        Long answer_id = 3L;
        //When
        when( answerService.update( any(AnswerCreationDTO.class) , anyLong()) )
                .thenThrow( new ResourceNotFoundException("Answer", "id", answer_id) );
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> answerController.update(answerCreationDTO, answer_id)
        );  
        //Then
        assertEquals("Answer not found with id: '3'", exception.getMessage());
    }

     */
}
