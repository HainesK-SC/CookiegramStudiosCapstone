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
 * </p>
 * <ul>
 * <li>ADMIN → /admin/dashboard</li>
 * <li>BAKER → /employee/dashboard</li>
 * <li>COURIER → /courier/dashboard</li>
 * </ul>
 * <p>
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
 * @version 1.0
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Handles successful authentication by redirecting to role-appropriate dashboard.
     * <p>
     * This method is automatically called by Spring Security after a user successfully
     * authenticates. It examines the user's granted authorities (roles) and performs
     * a redirect to the appropriate dashboard URL.
     * </p>
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

        // Get user's authorities (roles)
        var authorities = authentication.getAuthorities();

        // Determine redirect URL based on role
        String redirectUrl = determineRedirectUrl(authorities);

        // Perform the redirect
        response.sendRedirect(redirectUrl);
    }


    /**
     * Determines the appropriate redirect URL based on user's role.
     * <p>
     * Iterates through the user's granted authorities to find the first matching role
     * and returns the corresponding dashboard URL. The role names are expected to be
     * prefixed with "ROLE_" as per Spring Security conventions.
     * </p>
     *
     * @param authorities collection of granted authorities (roles) for the authenticated user
     * @return the redirect URL for the user's dashboard
     */
    private String determineRedirectUrl(Iterable<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            // check role and return appropriate dashboard URL - fix (cleaner)
            if ("ROLE_ADMIN".equals(role)) return "/admin/dashboard";
            if ("ROLE_BAKER".equals(role)) return "/employee/dashboard";
            if ("ROLE_COURIER".equals(role)) return "/courier/dashboard";
        }

        // default redirect if no recognize role is found
        // this shouldn't happen in any normal operation, since all users (excluding customers/visitors) has a role
        return "/";
    }
}
