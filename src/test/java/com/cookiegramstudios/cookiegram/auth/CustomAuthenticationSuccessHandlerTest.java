package com.cookiegramstudios.cookiegram.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.verify;


/**
 * Unit tests for {@link CustomAuthenticationSuccessHandler}.
 *
 * <p>
 * This test class verifies that users are redirected to the correct
 * dashboard or landing page based on their assigned role after
 * successful authentication.
 * </p>
 *
 * <p>
 * Roles tested:
 * <ul>
 *     <li>Admin → /admin/dashboard</li>
 *     <li>Employee → /employee/dashboard</li>
 *     <li>Unknown role → /</li>
 * </ul>
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-26
 * @version 1.0
 */

public class CustomAuthenticationSuccessHandlerTest {

    /**
     * The authentication success handler being tested.
     */
    private CustomAuthenticationSuccessHandler successHandler;

    @BeforeEach
    void setUp(){
        successHandler = new CustomAuthenticationSuccessHandler();
    }

    /**
     * Verifies that a user with ROLE_ADMIN is redirected
     * to the admin dashboard upon successful login.
     *
     * @throws IOException if redirect fails
     * @throws ServletException if servlet processing fails
     */
    @Test
    void redirectsAdminToAdminDashboard() throws IOException, ServletException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin@cookiegram.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/admin/dashboard");
    }

    /**
     * Verifies that a user with ROLE_EMPLOYEE is redirected
     * to the employee dashboard upon successful login.
     *
     * @throws IOException if redirect fails
     * @throws ServletException if servlet processing fails
     */
    @Test
    void redirectsEmployeeToEmployeeDashboard() throws IOException, ServletException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "employee@cookiegram.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
        );

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/employee/dashboard");
    }

    /**
     * Verifies that a user with an unrecognized role is redirected
     * to the default landing page.
     *
     * @throws IOException if redirect fails
     * @throws ServletException if servlet processing fails
     */
    @Test
    void redirectsUnknownRoleToLandingPage() throws IOException, ServletException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "user@cookiegram.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_SOMETHING_ELSE"))
        );

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/");
    }

}
