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
 * @date 2026-02-26
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAccessTest {

    /**
     * Mock MVC instance used to simulate HTTP requests
     * against secured endpoints.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies that the landing page is publicly accessible
     * without authentication.
     *
     * @throws Exception if request execution fails
     */    @Test
    void landingPage_isPublic() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    /**
     * Verifies that an unauthenticated (anonymous) user
     * is redirected to the login page when attempting
     * to access the employee dashboard.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void anonymousUser_cannotAccessEmployeeDashboard() throws Exception {
        mockMvc.perform(get("/employee/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Verifies that an unauthenticated (anonymous) user
     * is redirected to the login page when attempting
     * to access the admin dashboard.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void anonymousUser_cannotAccessAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Verifies that an authenticated user with EMPLOYEE role
     * is forbidden from accessing the admin dashboard.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void employee_cannotAccessAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }
}
