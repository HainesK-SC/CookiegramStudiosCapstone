package com.cookiegramstudios.cookiegram.common;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import com.cookiegramstudios.cookiegram.user.UserService;

/**
 * Basic tests for {@link PublicController}.
 * 
 * @author Matthew Samaha
 * @date 2026-02-27
 * @version 1.0
 */
@WebMvcTest(PublicController.class)
@AutoConfigureMockMvc(addFilters = false)
class PublicControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PublicPageModelHelper publicPageModelHelper;

    @Test
    void homePageIsAccessible() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

        verify(publicPageModelHelper).populateHomeModel(any(Model.class));
    }

    @Test
    void loginPageIsAccessible() throws Exception {
        mockMvc.perform(get("/login").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        verify(publicPageModelHelper).populateLoginModel(eq(null), eq(null), any(Model.class));
    }

    @Test
    void loginPageWithErrorParameter() throws Exception {
        mockMvc.perform(get("/login").param("error", "true").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        verify(publicPageModelHelper).populateLoginModel(eq("true"), eq(null), any(Model.class));
    }
}