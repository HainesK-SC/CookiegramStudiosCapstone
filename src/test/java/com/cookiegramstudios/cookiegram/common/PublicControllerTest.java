package com.cookiegramstudios.cookiegram.common;

import com.cookiegramstudios.cookiegram.promotion.PromotionService;
import com.cookiegramstudios.cookiegram.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Basic tests for {@link PublicController}.
 * @author Matthew Samaha
 * @date 2026-02-27
 * @version 1.0
 */
@WebMvcTest(PublicController.class)
public class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PromotionService promotionService;

    @MockitoBean
    private UserService userService;

    @Test
    void homePageIsAccessible() throws Exception {
        when(promotionService.getByIsActive(true)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void loginPageIsAccessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void loginPageWithErrorParameter() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }
}