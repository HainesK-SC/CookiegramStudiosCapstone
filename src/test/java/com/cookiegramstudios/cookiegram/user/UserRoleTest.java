package com.cookiegramstudios.cookiegram.user;

import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link UserRole}.
 *
 * <p>
 * Ensures that all expected user roles are present and accessible.
 * This acts as a safety test to detect accidental enum modification.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-26
 * @version 2.0
 */
public class UserRoleTest {

    /**
     * Verifies that all expected roles exist in the system.
     * Current Sprint 1 supported roles:
     * <ul>
     *     <li>ADMIN</li>
     *     <li>EMPLOYEE</li>
     * </ul>
     */
    @Test
    void testAllRolesExists(){
        assertEquals(2, UserRole.values().length); // current roles are 2
        assertNotNull(UserRole.ADMIN);
        assertNotNull(UserRole.EMPLOYEE);
    }
}
