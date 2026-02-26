package com.cookiegramstudios.cookiegram.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom authentication success handler that redirects users to role-specific dashboards.
 * <p>
 * After successful authentication, this handler examines the user's role and redirects
 * them to the appropriate dashboard:
 * <p>
 * Sprint 1 role model:
 * <ul>
 *     <li>EMPLOYEE -> /employee/dashboard</li>
 *     <li>ADMIN -> /admin/dashboard</li>
 * </ul>
 * If the user has multiple roles (unlikely in this system), the first role found
 * determines the redirect destination. If no recognized role is found, the user
 * is redirected to the home page.
 * </p>
 *
 * <p><b>References:</b></p>
 * <ul>
 *     <li>
 *         <a href="https://www.codejava.net/frameworks/spring-boot/spring-security-authentication-success-handler-examples">
 *             CodeJava – Spring Security Authentication Success Handler Examples
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://mainul35.medium.com/spring-security-demonstrating-custom-authentication-success-handler-3b6fcb572a53">
 *             Medium – Custom Authentication Success Handler
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://www.baeldung.com/spring-redirect-after-login">
 *             Baeldung – Redirect After Login
 *         </a>
 *     </li>
 * </ul>
 *
 * @author Matthew Samaha
 * @date 2026-02-23
 * @version 2.0
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Handles successful authentication by redirecting to role-appropriate dashboard.
     *
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the Authentication object containing user details and authorities
     * @throws IOException if an input or output error occurs during the redirect
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String redirectUrl = determineRedirectUrl(authentication.getAuthorities());
        response.sendRedirect(redirectUrl);
    }


    /**
     * Determines redirect URL based on granted authorities.
     *
     * @param authorities collection of granted authorities for the authenticated user
     * @return redirect URL
     */
    private String determineRedirectUrl(Iterable<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if ("ROLE_ADMIN".equals(role)) {
                return "/admin/dashboard";
            }
            if ("ROLE_EMPLOYEE".equals(role)) {
                return "/employee/dashboard";
            }
        }

        // Safe fallback for unexpected role/authority state
        return "/";
    }
}
