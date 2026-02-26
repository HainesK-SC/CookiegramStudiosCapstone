package com.cookiegramstudios.cookiegram.user;

/**
 * Defines system-level roles for user authentication.
 * <p>
 *     Each role grants specific access permissions and determines post-login redirect destination.
 *     The system supports three disctint roles with different levels of access and responsibilities
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-18
 * @version 1.1
 */
public enum UserRole {

    /**
     * Admin role
     * <p>
     * <b>Post-login redirect:</b> {@code /admin/dashboard/}
     * </p>
     */
    ADMIN,

    /**
     * Baker role for production-level employees.
     * <p>
     * <b>Post-login redirect:</b> {@code /employee/dashboard/}
     * </p>
     */
    EMPLOYEE,
}
