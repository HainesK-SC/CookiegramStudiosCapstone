package com.cookiegramstudios.cookiegram.user;

import com.cookiegramstudios.cookiegram.common.exceptions.UserAlreadyExistsException;
import com.cookiegramstudios.cookiegram.common.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for user-related business operations.
 * <p>
 * Provides application-facing methods for retrieving user data and acts as an
 * abstraction between controllers and {@link UserRepository}.
 * Additional user business logic should be centralized here as features expand.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 3.0
 */
@Service
@Transactional
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserValidationService userValidationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            logger.warn("User not found for email: {}", email);
            throw new UserNotFoundException("User not found for email: " + email);
        }

        logger.info("User found for email: {}", email);
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        logger.debug("Fetching user by id: {}", id);
        User user = requireUserById(id);
        logger.info("User found with ID: {}", id);
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.debug("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.info("All users fetched");
        return users;
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(UserRole role) {
        logger.debug("Fetching users by role: {}", role);
        List<User> users = userRepository.findAllByRole(role);
        logger.info("Users fetched by role: {}", role);
        return users;
    }

    public User createUser(User user) {
        logger.debug("Creating new user with email: {}", user != null ? user.getEmail() : null);

        userValidationService.validateForCreation(user);
        assertEmailAvailable(user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        logger.info("Successfully created user with ID: {} and email: {}",
                savedUser.getId(), savedUser.getEmail());

        return savedUser;
    }

    public User updateUser(Long id, User user) {
        logger.debug("Updating user with ID: {}", id);

        User existingUser = requireUserById(id);
        userValidationService.validateForUpdate(user);
        assertEmailAvailableForUpdate(existingUser.getEmail(), user.getEmail());

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());

        User updatedUser = userRepository.save(existingUser);
        logger.info("Successfully updated user with ID: {}", id);

        return updatedUser;
    }

    public void deleteUser(Long id) {
        logger.debug("Deleting user with ID: {}", id);

        User user = requireUserById(id);
        userRepository.deleteById(id);

        logger.info("Successfully deleted user with ID: {} and email: {}",
                id, user.getEmail());
    }

    private User requireUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found with ID: " + id);
                });
    }

    private void assertEmailAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            logger.warn("Attempted to create user with existing email: {}", email);
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }
    }

    private void assertEmailAvailableForUpdate(String currentEmail, String newEmail) {
        if (!currentEmail.equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            logger.warn("Attempted to update to existing email: {}", newEmail);
            throw new UserAlreadyExistsException(
                    "Another user already exists with email: " + newEmail
            );
        }
    }
}
