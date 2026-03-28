package com.cookiegramstudios.cookiegram.auth;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cookiegramstudios.cookiegram.order.Order;
import com.cookiegramstudios.cookiegram.order.OrderService;
import com.cookiegramstudios.cookiegram.user.User;
import com.cookiegramstudios.cookiegram.user.UserService;

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
    private final OrderService orderService;

    public AdminController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
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
        String email = principal.getName();
        User user = userService.findByEmail(email);

        List<Order> pendingOrders = orderService.findPending();

        model.addAttribute("user", user);
        model.addAttribute("pendingOrderCount", pendingOrders.size());
        return "admin/admin-dashboard";
    }

    @GetMapping("/orders")
    public String orders(
            @RequestParam(name = "tab", defaultValue = "pending") String tab,
            Principal principal,
            Model model
    ) {
        String email = principal.getName();
        User user = userService.findByEmail(email);

        List<Order> pendingOrders = orderService.findPending();
        List<Order> approvedOrders = orderService.findApprovedOrders();

        String activeTab = "approved".equalsIgnoreCase(tab) ? "approved" : "pending";

        model.addAttribute("user", user);
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("approvedOrders", approvedOrders);
        model.addAttribute("pendingOrderCount", pendingOrders.size());
        model.addAttribute("approvedOrderCount", approvedOrders.size());

        return "admin/admin-orders";
    }
}


