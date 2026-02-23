package com.cookiegramstudios.cookiegram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration class for the CookieGram application.
 * <p>
 * This class provides security-related beans and configurations,
 * including password encoding for secure password storage.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-23
 * @version 1.0
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
}
