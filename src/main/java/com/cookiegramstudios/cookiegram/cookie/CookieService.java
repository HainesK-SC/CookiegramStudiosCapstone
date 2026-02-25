package com.cookiegramstudios.cookiegram.cookie;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class CookieService {
    private static final Logger logger = LoggerFactory.getLogger(CookieService.class);

    private final CookieRepository cookieRepository;

    public CookieService(CookieRepository cookieRepo) {
        this.cookieRepository = cookieRepo;
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

    /**
     * Validates Cookie data.
     * Note: I've updated the logic to avoid the "always true/false"
     * pitfalls in the previous validation example.
     */
    private boolean validateCookieObject(Cookie cookie) throws InvalidCookieDataException {
        if (cookie == null) {
            throw new InvalidCookieDataException("Cookie object is null.");
        }

        // Validate Name
        if (cookie.getName() == null || cookie.getName().isBlank()) {
            throw new InvalidCookieDataException("Cookie name cannot be blank.");
        }

        // Validate Price
        if (cookie.getBasePrice() <= 0 || Double.isNaN(cookie.getBasePrice())) {
            logger.debug("Cookie ID: {}. Invalid basePrice: {}", cookie.getId(), cookie.getBasePrice());
            throw new InvalidCookieDataException("Base price must be greater than zero.");
        }

        return true;
    }
}
