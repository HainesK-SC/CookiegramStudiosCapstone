package com.cookiegramstudios.cookiegram.auth;

import com.cookiegramstudios.cookiegram.user.User;
import com.cookiegramstudios.cookiegram.user.UserRole;
import com.cookiegramstudios.cookiegram.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Basic tests for {@link AdminController}.
 * @author Matthew Samaha
 * @date 2026-02-27
 * @version 1.0
 */
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    
    // adminCanAccessAdminDashboard test
    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void adminCanAccessDashboard() throws Exception {
        User user = new User();
        user.setEmail("admin@test.com");
        user.setRole(UserRole.ADMIN);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());

        when(userService.findByEmail("admin@test.com")).thenReturn(user);

        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-dashboard"));
    }


}
