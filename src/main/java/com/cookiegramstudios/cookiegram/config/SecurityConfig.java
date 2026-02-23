package com.cookiegramstudios.cookiegram.config;

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
 * HTTP Security filter chain configurations
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-23
 * @version 1.1
 */
@Configuration
public class SecurityConfig {

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
     * <li>Form-based login with custom login page</li>
     * <li>Logout configuration with session invalidation</li>
     * <li>H2 console access (if applicable)</li>
     * </ul>
     * </p>
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

                        // Placeholder for role-based restrictions
                        // All other requests will require authentication for now
                        .anyRequest().authenticated()
                )

                // Configure form-based login - NO SpringSecurity standards
                .formLogin(form -> form
                                .loginPage("/login")                    // Custom login page URL
                                .loginProcessingUrl("/login")           // URL to submit username/password
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
