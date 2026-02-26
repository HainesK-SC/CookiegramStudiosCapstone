package com.cookiegramstudios.cookiegram.customization;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CookieCustomizationService {
    private static final Logger logger = LoggerFactory.getLogger(CookieCustomizationService.class);
    private final CookieCustomizationRepository customizationRepository;

    public CookieCustomizationService(CookieCustomizationRepository repo) {
        this.customizationRepository = repo;
    }

    @Transactional(readOnly = true)
    public List<CookieCustomization> getAllCustomizations() {
        return customizationRepository.findAll();
    }

    /**
     * Validates customization logic, including message length and costs.
     */
    private boolean validateCustomization(CookieCustomization custom) {
        if (custom == null) {
            throw new RuntimeException("Customization data is missing.");
        }

        // Validate Message Length (e.g., max 30 characters for a cookie)
        if (custom.getMessageText() != null && custom.getMessageText().length() > 30) {
            throw new RuntimeException("Message text is too long for the cookie surface.");
        }

        // Validate Additional Cost
        if (custom.getAdditionalCost() < 0) {
            throw new RuntimeException("Additional cost cannot be negative.");
        }

        return true;
    }
}
