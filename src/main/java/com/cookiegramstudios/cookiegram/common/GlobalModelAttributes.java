package com.cookiegramstudios.cookiegram.common;

import com.cookiegramstudios.cookiegram.user.User;
import com.cookiegramstudios.cookiegram.user.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

/**
 * Provides common model attributes to all MVC controllers.
 * <p>
 * This {@code @ControllerAdvice} component automatically injects shared data
 * into the model for every view rendered by Spring MVC controllers.
 * Currently adds the authenticated user object to all templates via the
 * {@code currentUser} attribute.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-27
 * @version 1.0
 */
@ControllerAdvice
public class GlobalModelAttributes {

    private final UserService userService;

    public GlobalModelAttributes(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User currentUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userService.findByEmail(principal.getName());
    }
}
