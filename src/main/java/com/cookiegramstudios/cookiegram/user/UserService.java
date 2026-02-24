package com.cookiegramstudios.cookiegram.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

/**
 * Service layer for user-related business operations.
 * <p>
 * Provides application-facing methods for retrieving user data and acts as an
 * abstraction between controllers and {@link UserRepository}.
 * Additional user business logic should be centralized here as features expand.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-23
 * @version 1.1
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Email validation pattern (regex)
    // this will be helpful when validating user input
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final int MN_PASSWORD_LENGTH = 6;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Finds a user by email address.
     * <p>
     * Email is the application's authentication identifier and is used
     * to resolve the currently authenticated user record.
     * </p>
     *
     * @param email email address of the user to retrieve
     * @return matching {@link User}, or {@code null} if not found
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }
}
