package com.cookiegramstudios.cookiegram.common;

import com.cookiegramstudios.cookiegram.promotion.Promotion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Controller for Home/Landing page endpoint, login page endpoint
@Controller
public class PublicController {

    @GetMapping("/")
    public String home(Model model){
        // mock promotions (will be replaced later with a proper service call)

        return "index";
    }


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
