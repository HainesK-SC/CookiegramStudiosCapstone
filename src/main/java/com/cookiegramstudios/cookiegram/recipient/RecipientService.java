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
}
