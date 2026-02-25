package com.cookiegramstudios.cookiegram.recipient;

import org.springframework.stereotype.Service;

/**
 * Service layer for recipient-related business operations.
 * <p>
 * Provides application-facing methods for managing recipients and acts as an
 * abstraction between controllers and {@link RecipientRepository}.
 * Handles recipient creation, retrieval, updates, and search operations.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
@Service
public class RecipientService {

    private final RecipientRepository recipientRepository;

    public RecipientService(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    // Very important -- ensures recipient inputs all required fields properly
    private void validateRecipient(Recipient recipient) {
        if (recipient.getName() == null || recipient.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient name is required");
        }
        if (recipient.getStreet() == null || recipient.getStreet().trim().isEmpty()) {
            throw new IllegalArgumentException("Street address is required");
        }
        if (recipient.getCity() == null || recipient.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        if (recipient.getPostalCode() == null || recipient.getPostalCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Postal code is required");
        }
        if (recipient.getCountry() == null || recipient.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Country is required");
        }
    }


}
