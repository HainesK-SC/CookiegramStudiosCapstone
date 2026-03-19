package com.cookiegramstudios.cookiegram.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User entity - tests validation constraints and business
 * logic
 */
public class UserTest {

    private Validator validator;
    private User validUser;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setPassword("password123");
        validUser.setRole(UserRole.ADMIN);
        validUser.setFirstName("John");
        validUser.setLastName("Doe");
    }

    @Test
    void validUser_hasNoViolations() {
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertTrue(violations.isEmpty());
    }

    @Test
    void emailNull_failsValidation() {
        validUser.setEmail(null);

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    void emailBlank_failsValidation() {
        validUser.setEmail("   ");

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    void passwordNull_failsValidation() {
        validUser.setPassword(null);

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    void passwordTooShort_failsValidation() {
        validUser.setPassword("12345");

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("Password must be at least 6 characters long", violations.iterator().next().getMessage());
    }

    @Test
    void roleNull_failsValidation() {
        validUser.setRole(null);

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("Role is required", violations.iterator().next().getMessage());
    }

    @Test
    void firstNameNull_failsValidation() {
        validUser.setFirstName(null);

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("First name is required", violations.iterator().next().getMessage());
    }

    @Test
    void firstNameBlank_failsValidation() {
        validUser.setFirstName(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("First name is required", violations.iterator().next().getMessage());
    }

    @Test
    void lastNameNull_failsValidation() {
        validUser.setLastName(null);

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("Last name is required", violations.iterator().next().getMessage());
    }

    @Test
    void lastNameBlank_failsValidation() {
        validUser.setLastName(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals("Last name is required", violations.iterator().next().getMessage());
    }

    @Test
    void onCreate_setsTimestampWhenNull() {
        assertNull(validUser.getCreatedAt());

        validUser.onCreate();

        assertNotNull(validUser.getCreatedAt());
    }

    @Test
    void toString_containsCoreFields() {
        validUser.setId(42L);

        String text = validUser.toString();

        assertTrue(text.contains("id=42"));
        assertTrue(text.contains("email='test@example.com'"));
        assertTrue(text.contains("role=ADMIN"));
        assertTrue(text.contains("firstName='John'"));
        assertTrue(text.contains("lastName='Doe'"));
    }
}
