package com.cookiegramstudios.cookiegram.cookie;
import com.cookiegramstudios.cookiegram.common.exceptions.InvalidCookieDataException;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service layer for cookie-related business operations.
 *
 * @name Nguyen Anh Khoa Tran
 * @name Matthew Samaha
 * @date 2026-03-18
 * @version 2.0
 */
@Service
public class CookieService {
	private static final Logger logger = LoggerFactory.getLogger(CookieService.class);

    private final CookieRepository cookieRepository;
    private final CookieValidationService cookieValidationService;

    public CookieService(CookieRepository cookieRepository,
                         CookieValidationService cookieValidationService) {
        this.cookieRepository = cookieRepository;
        this.cookieValidationService = cookieValidationService;
    }

    @Transactional(readOnly = true)
    public List<Cookie> getAllCookies() {
        logger.info("Retrieving all cookies...");
        return cookieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cookie> getById(long id) {
        logger.info("Retrieving Cookie with ID: {}", id);
        return cookieRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Cookie> getByIsActive(boolean isActive) {
        return cookieRepository.findByActive(isActive);
    }

    // Optional utility for future create/update flows
    protected void validateCookie(Cookie cookie) {
        cookieValidationService.validate(cookie);
    }
}
