package com.cookiegramstudios.cookiegram.user;

import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRoleTest {

    // Tests if all roles exists
    // Changed to reflect current roles for Sprint 1
    @Test
    void testAllRolesExists(){
        assertEquals(3, UserRole.values().length);
        assertNotNull(UserRole.ADMIN);
        assertNotNull(UserRole.EMPLOYEE);
    }
}
