package com.cookiegramstudios.cookiegram.user;


import com.cookiegramstudios.cookiegram.common.exceptions.InvalidUserDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Centralized validator for {@link User} create/update operations.
 * <p>
 * This service encapsulates input validation rules used by {@link UserService}
 * so business orchestration and validation concerns remain separated.
 * </p>
 *
 * <p><b>Validation coverage:</b></p>
 * <ul>
 *     <li>Create: email format, password presence/length, first name, last name, role</li>
 *     <li>Update: email format, first name, last name</li>
 * </ul>
 *
 * <p>
 * Validation failures throw {@link com.cookiegramstudios.cookiegram.common.exceptions.InvalidUserDataException}
 * with user-facing messages suitable for controller/service error handling.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
@Service
public class UserValidationService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserValidationService.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    public static final int MIN_PASSWORD_LENGTH = 6;

    public void validateForCreation(User user) {
        if (user == null) {
            throw new InvalidUserDataException("User cannot be null");
        }

        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateName(user.getFirstName(), "First name is required");
        validateName(user.getLastName(), "Last name is required");

        if (user.getRole() == null) {
            throw new InvalidUserDataException("Role is required");
        }

        logger.debug("User creation validation passed for email: {}", user.getEmail());
    }

    public void validateForUpdate(User user) {
        if (user == null) {
            throw new InvalidUserDataException("User object cannot be null");
        }

        validateEmail(user.getEmail());
        validateName(user.getFirstName(), "First name is required");
        validateName(user.getLastName(), "Last name is required");

        logger.debug("User update validation passed for email: {}", user.getEmail());
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserDataException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidUserDataException("Invalid email format: " + email);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new InvalidUserDataException("Password is required");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidUserDataException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long"
            );
        }
    }

    private void validateName(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidUserDataException(message);
        }
    }

}
