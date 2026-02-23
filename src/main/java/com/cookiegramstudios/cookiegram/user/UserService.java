package com.cookiegramstudios.cookiegram.user;

import org.springframework.stereotype.Service;
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
 * @version 1.0
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
