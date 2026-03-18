package com.cookiegramstudios.cookiegram.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
 * @author Kyle Haines 
 * @date 2026-03-18
 * @version 2.0
 */
@Controller
public class PublicController {
	
	private static final String VIEW_INDEX = "index";
    private static final String VIEW_LOGIN = "login";
    private static final String VIEW_ABOUT = "about";
    private static final String VIEW_FAQ = "faq";
    private static final String VIEW_CONTACT = "contact";
    private static final String VIEW_SHIPPING_POLICY = "shipping-policy";
    private static final String VIEW_PRIVACY_POLICY = "privacy-policy";
	
	private final PublicPageModelHelper pageModelHelper;

    public PublicController(PublicPageModelHelper pageModelHelper) {
        this.pageModelHelper = pageModelHelper;
    }

    @GetMapping("/")
    public String home(Model model) {
        pageModelHelper.populateHomeModel(model);
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        pageModelHelper.populateLoginModel(error, logout, model);
        return "login";
    }
    
    @GetMapping("/about")
    public String aboutPage() {
        return VIEW_ABOUT;
    }

    @GetMapping("/faq")
    public String faqPage() {
        return VIEW_FAQ;
    }

    @GetMapping("/contact")
    public String contactPage() {
        return VIEW_CONTACT;
    }

    @GetMapping("/shipping-policy")
    public String shippingPolicy() {
        return VIEW_SHIPPING_POLICY;
    }

    @GetMapping("/privacy-policy")
    public String privacyPolicy() {
        return VIEW_PRIVACY_POLICY;
    }

}
