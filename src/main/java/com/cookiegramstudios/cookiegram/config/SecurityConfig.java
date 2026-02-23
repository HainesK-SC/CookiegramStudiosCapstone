package com.cookiegramstudios.cookiegram.config;

import com.cookiegramstudios.cookiegram.auth.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the CookieGram application.
 * <p>
 * This class provides security-related beans and configurations,
 * including password encoding for secure password storage and
 * HTTP Security filter chain configurations with role-based access control
 * </p>
 *
 * <p><strong>References:</strong></p>
 * <ul>
 *     <li>
 *         <a href="https://www.codejava.net/frameworks/spring-boot/spring-security-authentication-success-handler-examples" target="_blank">
 *             CodeJava – Spring Security Authentication Success Handler Examples
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://mainul35.medium.com/spring-security-demonstrating-custom-authentication-success-handler-3b6fcb572a53" target="_blank">
 *             Medium – Custom Authentication Success Handler
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://www.baeldung.com/spring-redirect-after-login" target="_blank">
 *             Baeldung – Redirect After Login
 *         </a>
 *     </li>
 * </ul>
 *
 * @author Matthew Samaha
 * @date 2026-02-23
 * @version 1.4
 */
@Configuration
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    // Consturctor Injection - for custom auth success handling
    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    /**
     * Configures the password encoder bean for the entire application
     * <p>
     *     Uses BCrypt hashing algorithm for secure password storage.
     *     BCrypt uses an adaptive hashing function that includes salt generation and is resistant to brute force
     *     This should be feasible for our implementations' at this point.
     * </p>
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for HTTP requests.
     * <p>
     * This method sets up:
     * <ul>
     * <li>Public endpoints (home page, login, static resources)</li>
     * <li>Role-based access control for ADMIN, BAKER, and COURIER roles</li>
     * <li>Form-based login with custom login page and success handler</li>
     * <li>Logout configuration with session invalidation</li>
     * <li>H2 console access (development only)</li>
     * </ul>
     * </p>
     * <p>
     * <b>Role-Based Access:</b>
     * <ul>
     * <li>ADMIN: Full access to /admin/** endpoints</li>
     * <li>BAKER: Access to /employee/** endpoints</li>
     * <li>COURIER: Access to /courier/** endpoints</li>
     * </ul>
     * </p>
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configure authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Public endpoints - accessible to everyone (standard)
                        .requestMatchers(
                                "/",              // Home page
                                "/login",         // Login page
                                "/css/**",        // CSS files
                                "/js/**",         // JavaScript files
                                "/images/**",     // Image files
                                "/error"          // Error pages
                        ).permitAll()

                        // H2 Console access (for development only)
                        // Anyone can access
                        .requestMatchers("/h2-console/**").permitAll()

                        // Role-based access control - added :: redirect to URl if user has role
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/employee/**").hasRole("BAKER")
                        .requestMatchers("/courier/**").hasRole("COURIER")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Configure form-based login
                .formLogin(form -> form
                        .loginPage("/login")                    // Custom login page URL
                        .loginProcessingUrl("/login")           // URL to submit username/password
                        .successHandler(successHandler)         // Custom success handler for role-based redirects
                        .failureUrl("/login?error=true")        // Redirect on login failure
                        .permitAll()                            // Allow everyone to access login page
                )

                // Configure logout
                .logout(logout -> logout
                        .logoutUrl("/logout")                   // URL to trigger logout
                        .logoutSuccessUrl("/")                  // Redirect after successful logout
                        .invalidateHttpSession(true)            // Invalidate session on logout
                        .clearAuthentication(true)              // Clear authentication on logout
                        .deleteCookies("JSESSIONID")            // Delete session cookie
                        .permitAll()                            // Allow everyone to logout
                )

                // H2 Console specific settings (for development)
                // TODO: Remove in production
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")  // Disable CSRF for H2 console
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())  // Allow frames from same origin (for H2)
                );

        return http.build();
    }
}
