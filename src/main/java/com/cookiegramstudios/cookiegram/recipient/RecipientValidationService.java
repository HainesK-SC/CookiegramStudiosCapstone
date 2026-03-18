package com.cookiegramstudios.cookiegram.recipient;

import org.springframework.stereotype.Service;

/**
 * Handles validation rules for recipient create/update operations.
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
@Service
public class RecipientValidationService {
	
	public void validateRecipient(Recipient recipient) {
        if (recipient.getFirstName() == null || recipient.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient first name is required");
        }
        if (recipient.getLastName() == null || recipient.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient last name is required");
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
