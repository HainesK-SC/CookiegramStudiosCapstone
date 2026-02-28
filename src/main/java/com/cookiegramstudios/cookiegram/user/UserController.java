package com.cookiegramstudios.cookiegram.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for employee-restricted user endpoints.
 * <p>
 * Handles employee dashboard and related employee-specific views.
 * Access is restricted to users with the {@code EMPLOYEE} role.
 * Endpoint methods will be added incrementally as employee features are implemented.
 * </p>
 * <p>
 * <b>Base Path:</b> {@code /employee}
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-27
 * @version 1.0
 */
@Controller
@RequestMapping("/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class UserController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "employee/employee-dashboard";
    }
}
