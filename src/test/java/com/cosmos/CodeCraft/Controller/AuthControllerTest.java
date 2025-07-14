/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Dto.AuthResponse;
import com.cosmos.CodeCraft.Exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /* @Test
    public void testAccesoProtegidoSinToken() throws Exception {
        mockMvc.perform(get("/api/protegido"))
                .andExpect(status().isForbidden()); // 403
    }
    */



    @Test
    public void loginTest() throws Exception {
        String username = "admin";
        String password = "12345";
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";


        MvcResult result = mockMvc.perform(post("/auth/log-in")
                        .contentType("application/json")
                        .content(body))
                        .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        AuthResponse authResponse = mapper.readValue(json, AuthResponse.class);

        Assertions.assertEquals("admin", authResponse.username());
        Assertions.assertEquals("User logged!", authResponse.message());
        Assertions.assertNotNull(authResponse.token());
        Assertions.assertTrue(authResponse.status());
    }

    @Test
    public void loginTestFail() throws Exception {
        String username = "admin";
        String password = "123456";
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";


        MvcResult result = mockMvc.perform(post("/auth/log-in")
                        .contentType("application/json")
                        .content(body))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = mapper.readValue(json, ErrorResponse.class);

        Assertions.assertEquals("Bad credentials", errorResponse.getErrorCode());
        Assertions.assertEquals("admin bad password", errorResponse.getMessage());

    }

    @Test
    public void registerTest() throws Exception {

        String username = "master23";
        String password = "1234567";
        String roles = "[\"ADMIN\"]";

        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"authCreateRoleRequest\":{\"roles\":"+roles+"}}" ;

        MvcResult result = mockMvc.perform(post("/auth/sign-up")
                        .contentType("application/json")
                        .content(body))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        AuthResponse authResponse = mapper.readValue(json, AuthResponse.class);

        Assertions.assertEquals("master23", authResponse.username());
        Assertions.assertEquals("User created", authResponse.message());
        Assertions.assertNotNull(authResponse.token());
        Assertions.assertTrue(authResponse.status());
    }

    @Test
    public void registerTestFail() throws Exception {

        String username = "admin";
        String password = null;
        String roles = "[\"ROLE_ADMIN\"]";

        String body = "{\"username\":\"" + username + "\",\"password\":" + password + ",\"authCreateRoleRequest\":{\"roles\":"+roles+"}}" ;

        MvcResult result = mockMvc.perform(post("/auth/sign-up")
                        .contentType("application/json")
                        .content(body))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse error = mapper.readValue(json, ErrorResponse.class);

        Assertions.assertEquals("Argument not valid", error.getErrorCode());
        Assertions.assertEquals("Algun parametro no cumple las validaciones", error.getMessage());

    }






}
