package com.cookiegramstudios.cookiegram.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller for public-facing pages accessible to all users.
 * <p>
 * Handles requests for the home/landing page and login page.
 * These endpoints are configured as publicly accessible in SecurityConfig
 * and do not require authentication.
 * </p>
 * <p>
 * <b>Endpoints:</b>
 * </p>
 * <ul>
 * <li>GET / - Home/landing page with promotions</li>
 * <li>GET /login - Custom login page with error handling</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @date 2026-02-23
 * @version 1.0
 */
@Controller
public class PublicController {

    /**
     * Displays the home/landing page with current promotions.
     * <p>
     * This page is accessible to all users (authenticated or not) and serves as
     * the main entry point for the CookieGram application. It displays cookie orders, products & displays featured
     * promotions and provides navigation to other parts of the site.
     * </p>

     *
     * @param model Spring MVC Model to pass data to the view
     * @return view name "index" (maps to templates/index.html)
     */
    @GetMapping("/")
    public String home(Model model){
        // mock promotions (will be replaced later with a proper service call)
        List<String> promotionsList = List.of(
                "Buy 1 Dozen, Get 10% Off",
                "Free Delivery on orders over $30",
                "Weekend Special: 2 Cookies Free"
        );
        model.addAttribute("promotions", promotionsList);

        return "index";
    }

    /**
     * Displays the custom login page with error and logout message handling.
     * <p>
     * This endpoint serves the login form and handles feedback messages from
     * Spring Security:
     * </p>
     * <ul>
     * <li>error=true - Authentication failed (invalid credentials)</li>
     * <li>logout=true - User successfully logged out</li>
     * </ul>
     * <p>
     * The actual authentication is processed by Spring Security at POST /login.
     * Upon successful authentication, users are redirected to role-specific
     * dashboards via CustomAuthenticationSuccessHandler.
     * </p>
     *
     * @param error optional parameter indicating authentication failure
     * @param logout optional parameter indicating successful logout
     * @param model Spring MVC Model to pass data to the view
     * @return view name "login" (maps to templates/login.html)
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model)
        {

        // Check if login failed and add error message
        if (error != null) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        }

        // Check if user just logged out and add success message
        if (logout != null) {
            model.addAttribute("logout", true);
            model.addAttribute("logoutMessage", "You have been successfully logged out.");
        }

        return "login";

    }
}
