package com.cookiegramstudios.cookiegram.auth;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cookiegramstudios.cookiegram.user.UserRole;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 3.0
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private static final String REDIRECT_ADMIN_DASHBOARD = "/admin/dashboard";
    private static final String REDIRECT_EMPLOYEE_DASHBOARD = "/employee/dashboard";
    private static final String REDIRECT_HOME = "/";

    private static final Map<UserRole, String> ROLE_REDIRECTS = Map.of(
            UserRole.ADMIN, REDIRECT_ADMIN_DASHBOARD,
            UserRole.EMPLOYEE, REDIRECT_EMPLOYEE_DASHBOARD
    );
  
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String redirectUrl = determineRedirectUrl(authentication.getAuthorities());
        response.sendRedirect(redirectUrl);
    }

    private String determineRedirectUrl(Iterable<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            Optional<UserRole> role = toUserRole(authority.getAuthority());
            if (role.isPresent()) {
                String redirect = ROLE_REDIRECTS.get(role.get());
                if (redirect != null) {
                    return redirect; // first recognized role wins
                }
            }
        }
        return REDIRECT_HOME;
    }

    private Optional<UserRole> toUserRole(String authority) {
        for (UserRole role : UserRole.values()) {
            if (role.toAuthority().equals(authority)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }
}
