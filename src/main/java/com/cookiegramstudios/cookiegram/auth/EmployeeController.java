package com.cookiegramstudios.cookiegram.auth;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
 * @version 1.1
 */
@Controller
@RequestMapping("/employee/dashboard")
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
        
        // Retrieve today's orders for the bake list
        List<Order> todaysOrders = orderService.getTodaysOrders();
        
        model.addAttribute("user", user);
        model.addAttribute("orders", todaysOrders);
        
        return "employee/employee-dashboard";
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
