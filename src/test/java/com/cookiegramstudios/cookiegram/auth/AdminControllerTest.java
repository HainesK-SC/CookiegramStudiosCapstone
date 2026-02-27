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
 */
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    
    // adminCanAccessAdminDashboard test


}
