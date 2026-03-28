package com.cookiegramstudios.cookiegram.auth;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.cookiegramstudios.cookiegram.order.Order;
import com.cookiegramstudios.cookiegram.order.OrderService;
import com.cookiegramstudios.cookiegram.order.OrderStatus;
import com.cookiegramstudios.cookiegram.user.User;
import com.cookiegramstudios.cookiegram.user.UserService;

/**
 * Controller for employee (baker) pages and actions.
 * <p>
 * Handles requests for employee dashboard and baker-specific features.
 * Access is restricted to users with the EMPLOYEE (baker) role via {@code @PreAuthorize}.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-14
 * @version 1.2
 */
@Controller 
@RequestMapping("/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {
	
	private final UserService userService;
    private final OrderService orderService;

    public EmployeeController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
    	String email = principal.getName();
        User user = userService.findByEmail(email);

        List<Order> todaysApprovedOrders = orderService.getTodaysApprovedOrders();
        List<Order> otherApprovedOrders = orderService.getOtherApprovedOrders();

        model.addAttribute("user", user);

        // Keep this for compatibility if any fragment expects "orders"
        model.addAttribute("orders", todaysApprovedOrders);

        model.addAttribute("todaysApprovedOrders", todaysApprovedOrders);
        model.addAttribute("otherApprovedOrders", otherApprovedOrders);

        return "employee/employee-dashboard";
    }
    
    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable("id") Long orderId, Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.findByEmail(email);

        try {
            Order order = orderService.findApprovedOrderById(orderId);
            model.addAttribute("user", user);
            model.addAttribute("order", order);
            return "employee/order-details";
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }
    
    @PostMapping("/order/update-status")
    @ResponseBody
    public ResponseEntity<String> updateOrderStatus(
            @RequestParam Long orderId, 
            @RequestParam OrderStatus newStatus) {
        try {
            orderService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to update status: " + e.getMessage());
        }
    }
}
