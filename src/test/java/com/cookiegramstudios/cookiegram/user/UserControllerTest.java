package com.cookiegramstudios.cookiegram.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Basic tests for {@link UserController}.
 * @author Matthew Samaha
 * @date 2026-02-27
 * @version 1.0
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void employeeCanAccessDashboard() throws Exception {
        mockMvc.perform(get("/employee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/employee-dashboard"));
    }
}






