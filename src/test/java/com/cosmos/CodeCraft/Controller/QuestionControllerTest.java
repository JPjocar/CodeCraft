/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.DataProvider;
import com.cosmos.CodeCraft.Dto.AnswerResponseDTO;
import com.cosmos.CodeCraft.Dto.AuthResponse;
import com.cosmos.CodeCraft.Dto.QuestionCreationDTO;
import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Exception.ErrorResponse;
import com.cosmos.CodeCraft.Exception.InsufficientTagsException;
import com.cosmos.CodeCraft.Service.QuestionService;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    public void createTest() throws Exception {

        String title = "Pregunta sobre seguridad";
        String content = "Tengo un problema en la seguridad mi aplicacion, aparece este error...";
        String tags = "[\"java\",\"spring\"]";
        String token = obtenerToken("admin", "12345");

        String body = "{\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"tags\":" + tags + "}";

        MvcResult result = mockMvc.perform(post("/question")
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        QuestionResponseDTO question = mapper.readValue(json, QuestionResponseDTO.class);

        Assertions.assertEquals("Pregunta sobre seguridad", question.getTitle());
        Assertions.assertEquals("pregunta-sobre-seguridad", question.getSlug());
        Assertions.assertEquals("Tengo un problema en la seguridad mi aplicacion, aparece este error...", question.getContent());
        Assertions.assertFalse(question.getTags().isEmpty());
        Assertions.assertTrue(question.getAnswers().isEmpty());
    }


    @Test
    public void createTestFailTags() throws Exception {

        String title = "Pregunta sobre seguridad";
        String content = "Tengo un problema en la seguridad mi aplicacion, aparece este error...";
        String tags = "[]";
        String token = obtenerToken("admin", "12345");

        String body = "{\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"tags\":" + tags + "}";

        MvcResult result = mockMvc.perform(post("/question")
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse error = mapper.readValue(json, ErrorResponse.class);

        Assertions.assertEquals("Tags required max=5", error.getErrorCode());
        Assertions.assertEquals("Required 5 tags. Is found: 0 tags", error.getMessage());

    }

    // Acceder a una pregunta ( Solo usuarios autenticados )
    @Test
    public void showTest() throws Exception {

        String token = obtenerToken("admin", "12345");
        int questionId = 1;

        MvcResult result = mockMvc.perform(get("/question/"+questionId)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        QuestionResponseDTO question = mapper.readValue(json, QuestionResponseDTO.class);

        Assertions.assertEquals("How to use Hibernate?", question.getTitle());
        Assertions.assertEquals("how-to-hibernate", question.getSlug());
        Assertions.assertEquals("Best practices for Hibernate ORM", question.getContent());
        Assertions.assertFalse(question.getTags().isEmpty());
        Assertions.assertFalse(question.getAnswers().isEmpty());
    }


    @Test
    public void showTestFail() throws Exception {

        String token = obtenerToken("admin", "12345");
        int questionId = 15;

        MvcResult result = mockMvc.perform(get("/question/"+questionId)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse error = mapper.readValue(json, ErrorResponse.class);

        Assertions.assertEquals("Resource not found", error.getErrorCode());
        Assertions.assertEquals("Question not found with id: '15'", error.getMessage());

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
    private QuestionService questionService;
    
    @InjectMocks
    private QuestionController questionController;
    
    @Test
    public void testCreate2(){
        //Give
        QuestionCreationDTO qcdto = new QuestionCreationDTO("Titulo", "Contenido pregunta", Set.of("java", "javascript", "python"));
        //When
        when(questionService.create2( any(QuestionCreationDTO.class) ))
                .thenReturn( DataProvider.getQuestion() );
        QuestionResponseDTO questionResponseDTO = this.questionController.create2(qcdto);
        //Then
        assertEquals(1l, questionResponseDTO.getId());
        assertEquals("titulo question", questionResponseDTO.getTitle());
        assertEquals("titulo-question", questionResponseDTO.getSlug());
        assertEquals("content", questionResponseDTO.getContent());
        assertFalse(questionResponseDTO.getAnswers().isEmpty());
        assertFalse(questionResponseDTO.getComments().isEmpty());
        assertFalse(questionResponseDTO.getTags().isEmpty());
    }
    
    @Test
    public void testCreate2Fail(){
       //Give
        QuestionCreationDTO questionCreationDTO = new QuestionCreationDTO("Titulo", "Contenido pregunta", Set.of("java", "javascript", "python"));
        //When
        when(questionService.create2( any(QuestionCreationDTO.class) ))
                .thenThrow(new InsufficientTagsException(5));
        InsufficientTagsException exception = assertThrows(
                InsufficientTagsException.class,
                () -> questionController.create2(questionCreationDTO)
        );      
        //Then
        assertEquals("Required 5 tags. Is found: 5 tags", exception.getMessage() );         
    }
    
    
    @Test
    public void testUpdate(){
        //Give
        QuestionCreationDTO questionCreationDTO = new QuestionCreationDTO("Titulo", "Contenido pregunta", Set.of("java", "javascript", "python"));
        Long id = 2L;
        
        //When
        when(questionService.update( any(QuestionCreationDTO.class) , anyLong() ))
                .thenReturn( DataProvider.getUpdatedQuestion());
        QuestionResponseDTO questionResponseDTO = this.questionController.update(questionCreationDTO, id);
        
        //Then
        assertEquals("titulo updated", questionResponseDTO.getTitle());
        assertEquals("titulo-updated", questionResponseDTO.getSlug());
        assertEquals("content updated", questionResponseDTO.getContent());
    }
    
    @Test
    public void testUpdateFailTags(){
        //Give
        QuestionCreationDTO questionCreationDTO = new QuestionCreationDTO("Titulo", "Contenido pregunta", Set.of("java", "javascript", "python"));
        Long id = 2L;
        //When
        when(questionService.update(any(QuestionCreationDTO.class), anyLong() ))
                .thenThrow(new InsufficientTagsException(5));
        InsufficientTagsException exception = assertThrows(
                InsufficientTagsException.class,
                () -> questionController.update(questionCreationDTO, id)
        );      
        //Then
        assertEquals("Required 5 tags. Is found: 5 tags", exception.getMessage() );        
    }
    
    
    @Test
    public void testUpdateFailSlugs(){
        //Give
        QuestionCreationDTO questionCreationDTO = new QuestionCreationDTO("Titulo", "Contenido pregunta", Set.of("java", "javascript", "python"));
        Long id = 2L;
        //When
        when(questionService.update( any(QuestionCreationDTO.class) , anyLong() ))
                .thenThrow(new IllegalArgumentException("Slug already exists"));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> questionController.update(questionCreationDTO, id)
        );      
        //Then
        assertEquals("Slug already exists", exception.getMessage() );        
    }
    */

}
