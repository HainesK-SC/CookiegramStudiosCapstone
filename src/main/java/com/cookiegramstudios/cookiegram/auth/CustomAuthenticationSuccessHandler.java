package com.cookiegramstudios.cookiegram.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

// Custom Auth Handler will redirect users to role-specific dashboards
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // onAuthSuccess() - handles successful auth by redirecting to role-appropriate dashboard
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


    // determineRedirectUrl() - determines appropriate redurect URL based on user's role
    private String determineRedirectUrl(Iterable<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            // check role and return appropriate dashboard URL
            if (role.equals("ROLE_ADMIN")){
                return "/admin/dashboard";
            } else if (role.equals("ROLE_BAKER")){
                return "/employee/dashboard";
            } else if (role.equals("ROLE_COURIER")){
                return "/courier/dashboard";
            }
        }

        // default redirect if no recognize role is found
        // this shouldn't happen in any normal operation, since all users (excluding customers/visitors) has a role
        return "/";
    }
}
