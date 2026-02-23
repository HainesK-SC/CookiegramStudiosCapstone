package com.cookiegramstudios.cookiegram.auth;

import com.cookiegramstudios.cookiegram.user.User;
import com.cookiegramstudios.cookiegram.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Controller for admin-only pages and actions.
 * <p>
 * Handles requests for administrator dashboard and future administrative features.
 * Access is restricted to users with the ADMIN role via {@code @PreAuthorize}
 * and reinforced by route-based rules in {@code SecurityConfig}.
 * </p>
 * <p>
 * <b>Current Endpoints:</b>
 * </p>
 * <ul>
 * <li>GET /admin/dashboard - Admin dashboard view with authenticated user details</li>
 * </ul>
 * <p>
 * <b>Planned Endpoints (TODO):</b>
 * </p>
 * <ul>
 * <li>GET /admin/users - User management</li>
 * <li>GET /admin/orders - All orders overview</li>
 * <li>GET /admin/reports - System reporting</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @date 2026-02-23
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the admin dashboard for the currently authenticated admin user.
     * <p>
     * Extracts the authenticated principal's username (email in this application),
     * retrieves the full {@link User} record, and adds it to the model for view rendering.
     * </p>
     *
     * @param principal currently authenticated user principal (email-based username)
     * @param model Spring MVC model for passing attributes to the view
     * @return view name {@code "admin/admin-dashboard"}
     */
    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        String email = principal.getName(); // principal username = email in your setup
        User user = userService.findByEmail(email);

        model.addAttribute("user", user);
        return "admin/admin-dashboard";
    }

    // TODO: Implement admin user management page
    // @GetMapping("/users")
    // public String users() { return "admin/users"; }

    // TODO: Implement admin orders overview page
    // @GetMapping("/orders")
    // public String orders() { return "admin/orders"; }

    // TODO: Implement admin reports page
    // @GetMapping("/reports")
    // public String reports() { return "admin/reports"; }

}


