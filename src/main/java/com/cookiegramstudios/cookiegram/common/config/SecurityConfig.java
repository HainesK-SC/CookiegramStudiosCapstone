package com.cookiegramstudios.cookiegram.common.config;

import com.cookiegramstudios.cookiegram.auth.CustomAuthenticationSuccessHandler;
import com.cookiegramstudios.cookiegram.user.UserRole;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
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
 * @version 1.6
 */
@Configuration
public class SecurityConfig {
	
	private static final String[] PUBLIC_ENDPOINTS = {
            "/",
            "/order/**",
            "/login",
            "/about",
            "/contact",
            "/faq",
            "/shipping-policy",
            "/privacy-policy",
            "/css/**",
            "/js/**",
            "/images/**",
            "/error"
    };

    
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(
            HttpSecurity http,
            CustomAuthenticationSuccessHandler successHandler
    ) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/employee/**").hasRole(UserRole.EMPLOYEE.name())
                        .requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(Customizer.withDefaults());

        // CSRF remains enabled by default for app endpoints.
        return http.build();
    }

    /**
     * Dev-only filter chain for H2 console access.
     */
    @Configuration
    @Profile("dev")
    static class DevH2SecurityConfig {

        @Bean
        @Order(1)
        public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .securityMatcher("/h2-console/**")
                    .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                    .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                    .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

            return http.build();
        }
    }
}
