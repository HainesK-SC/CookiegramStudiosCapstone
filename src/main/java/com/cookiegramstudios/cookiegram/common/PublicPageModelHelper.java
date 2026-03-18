package com.cookiegramstudios.cookiegram.common;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.cookiegramstudios.cookiegram.promotion.Promotion;
import com.cookiegramstudios.cookiegram.promotion.PromotionService;

@Component
public class PublicPageModelHelper {

	
	private static final String ATTR_ACTIVE_PROMOTIONS = "activePromotions";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_ERROR_MESSAGE = "errorMessage";
    private static final String ATTR_LOGOUT = "logout";
    private static final String ATTR_LOGOUT_MESSAGE = "logoutMessage";

    private static final String MSG_LOGIN_ERROR = "Invalid email or password. Please try again.";
    private static final String MSG_LOGOUT_SUCCESS = "You have been successfully logged out.";

    private final PromotionService promotionService;

    public PublicPageModelHelper(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    public void populateHomeModel(Model model) {
        List<Promotion> promotions = promotionService.getByIsActive(true);
        model.addAttribute(ATTR_ACTIVE_PROMOTIONS, promotions);
    }

    public void populateLoginModel(String error, String logout, Model model) {
        if (error != null) {
            model.addAttribute(ATTR_ERROR, true);
            model.addAttribute(ATTR_ERROR_MESSAGE, MSG_LOGIN_ERROR);
        }
        if (logout != null) {
            model.addAttribute(ATTR_LOGOUT, true);
            model.addAttribute(ATTR_LOGOUT_MESSAGE, MSG_LOGOUT_SUCCESS);
        }
    }

}
