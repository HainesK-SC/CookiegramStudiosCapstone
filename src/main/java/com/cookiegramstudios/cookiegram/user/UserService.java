package com.cookiegramstudios.cookiegram.user;

import com.cookiegramstudios.cookiegram.common.exceptions.InvalidUserDataException;
import com.cookiegramstudios.cookiegram.common.exceptions.UserAlreadyExistsException;
import com.cookiegramstudios.cookiegram.common.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
 * @version 2.1
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

    public static final int MIN_PASSWORD_LENGTH = 6;

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

    /**
     * Retrieves a user by their email address.
     * <p>
     * This method throws an exception if the user is not found,
     * unlike {@link #findByEmail(String)} which returns null.
     * </p>
     *
     * @param email the email address to search for
     * @return the User entity matching the email
     * @throws UserNotFoundException if user not found
     */
    @Transactional
    public User getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email);

        if (user == null){
            logger.warn("User not found for email: {}", email);
            throw new UserNotFoundException("User not found for email: " + email);
        }
        logger.info("User found for email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the user's unique identifier
     * @return the User entity
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id){
        logger.debug("Fetching user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found with ID: " + id);
                });

        logger.info("User found with ID: {}", id);
        return user;
    }

    /**
     * Retrieves all users in the system.
     * <p>
     * Primarily used for admin user management features.
     * </p>
     *
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        logger.debug("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.info("All users fetched");
        return users;
    }

    /**
     * Retrieves all users with a specific role.
     *
     * @param role the role to filter by
     * @return list of users with the specified role
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(UserRole role){
        logger.debug("Fetching users by role: {}", role);
        List<User> users = userRepository.findAllByRole(role);
        logger.info("Users fetched by role: {}", role);
        return users;
    }

    /**
     * Creates a new user account.
     * <p>
     * This method handles password encoding and timestamp generation automatically.
     * </p>
     *
     * @param user the user entity to create (password will be encoded)
     * @return the created and persisted user entity
     * @throws UserAlreadyExistsException if email already exists
     * @throws InvalidUserDataException if validation fails
     */
    public User createUser(User user){
        logger.debug("Creating new user with email: {}", user.getEmail());

        // validate user data before saving
        validateUserForCreation(user);

        // check if user already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logger.warn("Attempted to create user with existing email: {}", user.getEmail());
            throw new UserAlreadyExistsException(
                    "User already exists with email: " + user.getEmail()
            );
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        logger.debug("Password encoded for user: {}", user.getEmail());

        // Set creation timestamp (though @PrePersist also handles this)
        user.setCreatedAt(LocalDateTime.now());

        // Save user
        User savedUser = userRepository.save(user);
        logger.info("Successfully created user with ID: {} and email: {}",
                savedUser.getId(), savedUser.getEmail());

        return savedUser;

    }

    /**
     * Updates an existing user's information.
     * <p>
     * Updates firstName, lastName, and email. Does not update password or role.
     * </p>
     *
     * @param id the ID of the user to update
     * @param user the user entity containing updated information
     * @return the updated user entity
     * @throws UserNotFoundException if user not found
     * @throws InvalidUserDataException if validation fails
     * @throws UserAlreadyExistsException if new email already exists
     */
    public User updateUser(Long id, User user){
        logger.debug("Updating user with ID: {}", id);

        // Fetch existing user
        User existingUser = getUserById(id);

        // Validate updated data
        validateUserForUpdate(user);

        // Check if new email already exists (if email is being changed)
        if (!existingUser.getEmail().equals(user.getEmail())) {
            User userWithNewEmail = userRepository.findByEmail(user.getEmail());
            if (userWithNewEmail != null) {
                logger.warn("Attempted to update to existing email: {}", user.getEmail());
                throw new UserAlreadyExistsException(
                        "Another user already exists with email: " + user.getEmail()
                );
            }
        }

        // Update allowed fields
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        // Note: Role and password are NOT updated here for security

        // Save updated user
        User updatedUser = userRepository.save(existingUser);
        logger.info("Successfully updated user with ID: {}", id);

        return updatedUser;

    }

    public void deleteUser(Long id){
        logger.debug("Deleting user with ID: {}", id);

        // verify user exists before deleting
        User user = getUserById(id);

        // delete user
        userRepository.deleteById(id);
        logger.info("Successfully deleted user with ID: {} and email: {}",
                id, user.getEmail());
    }



    /**
     * VALIDATION METHODS
     */



    /**
     * Validates user data for creation.
     *
     * @param user the user to validate
     * @throws InvalidUserDataException if validation fails
     */
    private void validateUserForCreation(User user){

        if (user == null){
            throw new InvalidUserDataException("User cannot be null");
        }

        // Validate email
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new InvalidUserDataException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new InvalidUserDataException("Invalid email format: " + user.getEmail());
        }

        // Validate password
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidUserDataException("Password is required");
        }
        if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidUserDataException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long"
            );
        }

        // Validate first name
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new InvalidUserDataException("First name is required");
        }

        // Validate last name
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new InvalidUserDataException("Last name is required");
        }

        // Validate role
        if (user.getRole() == null) {
            throw new InvalidUserDataException("Role is required");
        }

        logger.debug("User validation passed for email: {}", user.getEmail());

    }

    /**
     * Validates user data for update operations.
     *
     * @param user the user to validate
     * @throws InvalidUserDataException if validation fails
     */
    private void validateUserForUpdate(User user){

        if (user == null) {
            throw new InvalidUserDataException("User object cannot be null");
        }

        // Validate email
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new InvalidUserDataException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new InvalidUserDataException("Invalid email format: " + user.getEmail());
        }

        // Validate first name
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new InvalidUserDataException("First name is required");
        }

        // Validate last name
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new InvalidUserDataException("Last name is required");
        }

        logger.debug("User update validation passed for email: {}", user.getEmail());

    }
}
