package com.cosmos.CodeCraft.Controller2;


import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private Slugify slg;

    @Test
    public void createTest() throws Exception {
        String title = "como configurar servidor tomcat";
        String content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        String tags = "[\"java\",\"spring\"]";
        String token = obtenerToken("admin", "admin");
        String body = "{\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"tags\":" + tags + "}";

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/question")
                        .content(body)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();


        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);
        JsonNode tagsNode = responseJson.get("tags");
        JsonNode commentsNode = responseJson.get("comments");
        JsonNode answerNode = responseJson.get("answers");
        JsonNode userNode = responseJson.get("user");

        assertThat(responseJson.get("id").asLong()).isInstanceOf(Long.class);
        assertEquals("como configurar servidor tomcat", responseJson.get("title").asText());
        assertEquals("como-configurar-servidor-tomcat", responseJson.get("slug").asText());
        assertEquals("Lorem Ipsum is simply dummy text of the printing and typesetting industry.", responseJson.get("content").asText());
        assertEquals(2, tagsNode.size());
        assertTrue(commentsNode.isEmpty());
        assertTrue(answerNode.isEmpty());
        assertEquals("admin", userNode.get("username").asText());

    }


    // La pregunta ya existe
    @Test
    public void createQuestionAlreadyExistsExceptionTest() throws Exception {
        String title = "spring boot config";
        String content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        String tags = "[\"java\",\"spring\"]";
        String token = obtenerToken("admin", "admin");
        String body = "{\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"tags\":" + tags + "}";

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/question")
                        .content(body)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        assertEquals("Question duplicated", responseJson.get("errorCode").asText());
        assertEquals("Question already exists", responseJson.get("message").asText());

        Optional<QuestionEntity> questions = this.questionRepository.findQuestionEntityBySlug(slg.slugify(title));
        assertTrue(questions.isPresent());

    }

    //Los tags estan excedidos
    @Test
    public void createExceededTagsExceptionTest() throws Exception {
        String title = "spring boot config 2";
        String content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        String tags = "[\"java\",\"spring\",\"typescript\",\"sqlserver\",\"postgresql\",\"nodejs\"]";
        String token = obtenerToken("admin", "admin");
        String body = "{\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"tags\":" + tags + "}";

        //Act
        MvcResult mvcResult = mockMvc.perform(post("/question")
                        .content(body)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        assertEquals("Exceeded Tags Exception", responseJson.get("errorCode").asText());
        assertEquals("Maximo 5 tags por pregunta", responseJson.get("message").asText());



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
