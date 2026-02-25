package com.cookiegramstudios.cookiegram.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User entity
 * - tests validation constraints and business logic
 */

public class UserTest {

    private Validator validator;
    private User validUser;

    // Before each test, create a new User instance
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = (Validator) factory.getValidator();

        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setPassword("password123");
        validUser.setRole(UserRole.ADMIN);
        validUser.setFirstName("John");
        validUser.setLastName("Doe");
    }

    @Test
    void testValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEmailRequired() {
        validUser.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPasswordMinLength() {
        validUser.setPassword("12345");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertEquals(1, violations.size());
    }

    @Test
    void testOnCreateSetsTimestamp() {
        assertNull(validUser.getCreatedAt());
        validUser.onCreate();
        assertNotNull(validUser.getCreatedAt());
    }
}
