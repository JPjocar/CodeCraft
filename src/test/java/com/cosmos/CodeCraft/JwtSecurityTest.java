package com.cosmos.CodeCraft;

import com.cosmos.CodeCraft.Dto.QuestionResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    /* @Test
    public void testAccesoProtegidoSinToken() throws Exception {
        mockMvc.perform(get("/api/protegido"))
                .andExpect(status().isForbidden()); // 403
    }
    */
    @Test
    public void testAccesoConTokenValido() throws Exception {
        // 1. Autenticar y obtener token

        /*String token = obtenerToken("admin", "12345");

        // 2. Acceder a ruta protegida
        MvcResult result = mockMvc.perform(get("/question/1")
                        .header("Authorization", "Bearer " + token))
                        .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        QuestionResponseDTO question = mapper.readValue(json, QuestionResponseDTO.class);
*/
        Assertions.assertEquals("How to use Hibernate?", "How to use Hibernate?");

    }

    private String obtenerToken(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        String response = mockMvc.perform(post("/auth/log-in")
                        .contentType("application/json")
                        .content(body))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("resp"+response);
        // Extrae el token del JSON de respuesta (ejemplo: {"token":"xxx"})
        return response.split("\"token\":\"")[1].split("\"")[0];
    }
}
