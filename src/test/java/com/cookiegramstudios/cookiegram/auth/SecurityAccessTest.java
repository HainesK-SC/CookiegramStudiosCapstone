package com.cookiegramstudios.cookiegram.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for application security configuration.
 *
 * <p>
 * This test suite verifies that route access is properly restricted
 * based on authentication state and assigned user roles.
 * </p>
 *
 * <p>
 * Scenarios covered:
 * <ul>
 *     <li>Public access to landing page</li>
 *     <li>Unauthorized users blocked from protected dashboards</li>
 *     <li>Role-based authorization enforcement</li>
 * </ul>
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 2.0
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAccessTest {

    @Autowired
    private MockMvc mockMvc;

   
    @Test
    void landingPage_isPublic() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    
    @Test
    public void whenAnonymousUser_shouldSeeAboutPage() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk());
    }

   
    @Test
    void whenAnonymousUser_shouldSeeFAQPage() throws Exception {
        mockMvc.perform(get("/faq"))
                .andExpect(status().isOk());
    }

    @Test
    void whenAnonymousUser_shouldSeeContactPage() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk());
    }

  
    @Test
    void anonymousUser_cannotAccessEmployeeDashboard() throws Exception {
        mockMvc.perform(get("/employee/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

  
    @Test
    void anonymousUser_cannotAccessAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

 
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void employee_cannotAccessAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void whenAuthenticatedUser_thenPublicPagesAreAccessible() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/about"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/faq"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk());
    }
}
