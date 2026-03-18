package com.cookiegramstudios.cookiegram.cookie;

import com.cookiegramstudios.cookiegram.common.exceptions.InvalidCookieDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Centralized validation logic for Cookie entities.
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
@Service
public class CookieValidationService {
	
	private static final Logger logger = LoggerFactory.getLogger(CookieValidationService.class);

    public void validate(Cookie cookie) {
        if (cookie == null) {
            throw new InvalidCookieDataException("Cookie object is null.");
        }

        if (cookie.getName() == null || cookie.getName().isBlank()) {
            throw new InvalidCookieDataException("Cookie name cannot be blank.");
        }

        if (cookie.getBasePrice() <= 0 || Double.isNaN(cookie.getBasePrice())) {
            logger.debug("Cookie ID: {}. Invalid basePrice: {}", cookie.getId(), cookie.getBasePrice());
            throw new InvalidCookieDataException("Base price must be greater than zero.");
        }
    }

}
