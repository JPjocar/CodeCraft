package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.cosmos.CodeCraft.Dto.TagResponseDTO;
import com.cosmos.CodeCraft.Entity.TagEntity;
import com.cosmos.CodeCraft.Exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createTest() throws Exception {

        String name = "javascript";
        String description = "Description for Javascript 2025";
        String body = "{\"name\":\"" + name + "\",\"description\":\"" + description + "\"}";

        String username = "admin";
        String password = "12345";
        String token = obtenerToken(username, password);

        MvcResult result = mockMvc.perform(post("/tag")
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        TagResponseDTO tag = mapper.readValue(json, TagResponseDTO.class);

        Assertions.assertEquals(name, tag.getName());
        Assertions.assertEquals(description, tag.getDescription());

    }

    @Test
    public void createTestFail() throws Exception {

        String name = "java";
        String description = "Description for Javascript 2025";
        String body = "{\"name\":\"" + name + "\",\"description\":\"" + description + "\"}";

        String username = "admin";
        String password = "12345";
        String token = obtenerToken(username, password);

        MvcResult result = mockMvc.perform(post("/tag")
                        .contentType("application/json")
                        .content(body)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse error = mapper.readValue(json, ErrorResponse.class);

        Assertions.assertEquals("Bad argument", error.getErrorCode());
        Assertions.assertEquals("el tag ya existe", error.getMessage());

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
}
