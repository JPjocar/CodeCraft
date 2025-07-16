package com.cosmos.CodeCraft.Controller2;

import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Entity.UserEntity;
import com.cosmos.CodeCraft.Entity.VoteEntity;
import com.cosmos.CodeCraft.Repository.AnswerRepository;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import com.cosmos.CodeCraft.Repository.UserRepository;
import com.cosmos.CodeCraft.Repository.VoteRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    //Marcar respuesta como solucion
    @Test
    public void isSolutionTest() throws Exception {
        //DATOS
        long answer_id = 1;
        String token = obtenerToken("admin", "admin");

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/answer/solution/" + answer_id)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        //Verificacion
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // VERIFICACIÓN 2: Cuerpo de la respuesta
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        assertTrue(responseJson.get("_correct").asBoolean());
        assertEquals(answer_id, responseJson.get("id").asLong());

        // VERIFICACIÓN 3: en base de datos
        AnswerEntity updatedAnswer = answerRepository.findById(answer_id).orElseThrow();
        assertTrue(updatedAnswer.is_correct());

    }

    //Marcar respuesta como solucion
    @Test
    public void isSolutionTestQuestionOwnershipException() throws Exception {
        //DATOS
        long answer_id = 1;
        String token = obtenerToken("moderator", "admin");

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/answer/solution/" + answer_id)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        //Verificacion
        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());

        // VERIFICACIÓN 2
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        System.out.println(responseJson.get("errorCode"));
        assertEquals("\"Question Ownership Exception\"", responseJson.get("errorCode").toString());
        assertEquals("\"Solo el creador puede marcar la respuesta como correcta\"", responseJson.get("message").toString());

        // VERIFICACIÓN 3
        AnswerEntity updatedAnswer = answerRepository.findById(answer_id).orElseThrow();
        assertFalse(updatedAnswer.is_correct());

    }

    @Test
    public void isSolutionTestResourceNotFoundException() throws Exception {
        //DATOS
        this.answerRepository.deleteById(1L);
        long answer_id = 1;
        String token = obtenerToken("admin", "admin");

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/answer/solution/" + answer_id)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        //Verificacion
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());

        // VERIFICACIÓN 2: Cuerpo de la respuesta
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        assertEquals("\"Resource not found\"", responseJson.get("errorCode").toString());
        assertEquals("\"answer not found with id: '1'\"", responseJson.get("message").toString());

        // VERIFICACIÓN 3: Estado en base de datos
        Optional<AnswerEntity> updatedAnswer = answerRepository.findById(answer_id);
        assertTrue(updatedAnswer.isEmpty());

    }


    //---------------------------------------------------------------------------------------
    //Realizar votos para las respuestas

    //Vota una respuesta que no le pertenece
    @Test
    public void voteTest() throws Exception {
        //DATOS
        long user_owner_id = 2; //moderator
        long answer_id = 5;
        boolean util = true;
        String token = obtenerToken("admin", "admin");

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/answer/vote/" + answer_id +"/"+util)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        //Verificacion
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // VERIFICACIÓN 2: Cuerpo de la respuesta
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        assertEquals(answer_id, responseJson.get("answerResponseDTO").get("id").asLong());
        assertEquals("admin", responseJson.get("userResponseDTO").get("username").asText());
        assertTrue(responseJson.get("util").asBoolean());

        //VERIFICACION 3
        UserEntity user = userRepository.findById(user_owner_id).get();
        assertEquals(2, user.getReputation());
        AnswerEntity answer = answerRepository.findById(answer_id).get();
        assertEquals(1, answer.getScore());
    }

    //Vota su propia respuesta
    @Test
    public void voteSelfVotingExceptionTest() throws Exception {
        //DATOS
        long user_owner_id = 1; // admin id
        long answer_id = 2;
        boolean util = true;
        String token = obtenerToken("admin", "admin");

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/answer/vote/" + answer_id +"/"+util)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        //Verificacion
        assertEquals(HttpStatus.FORBIDDEN.value(), mvcResult.getResponse().getStatus());

        // VERIFICACIÓN 2: Cuerpo de la respuesta
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        assertEquals("Self Voting Exception", responseJson.get("errorCode").asText());
        assertEquals("No puedes votar tu misma respuesta", responseJson.get("message").asText());

        //VERIFICACION NO APLICO CAMBIOS BD
        List<VoteEntity> updatedVote = voteRepository.findByUserEntity_UsernameAndAnswerEntity_Id("admin", answer_id);
        assertTrue(updatedVote.isEmpty());
        //VERIFICACION 3
        UserEntity user = userRepository.findById(user_owner_id).get();
        assertEquals(0, user.getReputation());
        AnswerEntity answer = answerRepository.findById(answer_id).get();
        assertEquals(0, answer.getScore());
    }

    // Usuario intenta votar mas de una vez
    @Test
    public void voteInvalidDoubleVoteExceptionTest() throws Exception {
        //DATOS
        long user_owner_id = 2; //moderator
        long answer_id = 4;
        boolean util = true;
        String token = obtenerToken("admin", "admin");

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/answer/vote/" + answer_id +"/"+util)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        //Verificacion
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());

        // VERIFICACIÓN 2: Cuerpo de la respuesta
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        assertEquals("Invalid Double Vote Exception", responseJson.get("errorCode").asText());
        assertEquals("No puedes votar dos veces una respuesta", responseJson.get("message").asText());

        //VERIFICACION NO APLICO CAMBIOS BD
        List<VoteEntity> updatedVote = voteRepository.findByUserEntity_UsernameAndAnswerEntity_Id("admin", answer_id);
        assertEquals(1, updatedVote.size());
        //VERIFICACION 3
        UserEntity user = userRepository.findById(user_owner_id).get();
        assertEquals(0, user.getReputation());
        AnswerEntity answer = answerRepository.findById(answer_id).get();
        assertEquals(0, answer.getScore());
    }


    private String obtenerToken(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        String response = mockMvc.perform(post("/auth/log-in")
                        .contentType("application/json")
                        .content(body))
                .andReturn()
                .getResponse()
                .getContentAsString();

        try{
            return response.split("\"token\":\"")[1].split("\"")[0];
        }catch (IndexOutOfBoundsException ex){
            return "invalid token";
        }
    }

}
