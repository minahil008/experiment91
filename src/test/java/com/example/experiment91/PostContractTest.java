package com.example.experiment91;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CONTRACT: the frontend relies on these exact field names and types.
 * Create response : { id:number, title:string, content:string, platform:string }
 * List response   : array of the same objects
 * Error response  : { error:string }
 */
@SpringBootTest
@AutoConfigureMockMvc
class PostContractTest {

    private static final String VALID_BODY =
            "{\"title\":\"Contract\",\"content\":\"Checking the contract\",\"platform\":\"LinkedIn\"}";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPostResponseMatchesFrontendContract() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.content").isString())
                .andExpect(jsonPath("$.platform").isString())
                .andExpect(jsonPath("$.text").doesNotExist());
    }

    @Test
    void listPostsResponseIsAnArrayWithAgreedFields() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].content").isString());
    }

    @Test
    void errorResponseMatchesFrontendContract() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"content\":\"x\",\"platform\":\"Twitter\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString());
    }
}