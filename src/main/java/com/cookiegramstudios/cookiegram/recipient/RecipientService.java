package com.cookiegramstudios.cookiegram.recipient;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
 * @version 1.1
 */
@Service
public class RecipientService {

    private final RecipientRepository recipientRepository;

    public RecipientService(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    /**
     * CRUD Methods
     * - create
     * - update
     * - delete
     */

    @Transactional
    public Recipient createRecipient(Recipient recipient) {
        validateRecipient(recipient);
        return recipientRepository.save(recipient);
    }

    @Transactional
    public Recipient updateRecipient(Recipient recipient) {
        if (recipient.getId() == null || !recipientRepository.existsById(recipient.getId())) {
            throw new IllegalArgumentException("Recipient does not exist with ID: " + recipient.getId());
        }
        validateRecipient(recipient);
        return recipientRepository.save(recipient);
    }

    @Transactional
    public void deleteRecipient(Long id) {
        if (!recipientRepository.existsById(id)) {
            throw new IllegalArgumentException("Recipient not found with ID: " + id);
        }
        recipientRepository.deleteById(id);
    }

    /**
     * General Methods
     */
    public Optional<Recipient> findById(Long id) {
        return recipientRepository.findById(id);
    }

    public List<Recipient> findAllRecipients() {
        return recipientRepository.findAll();
    }

    public List<Recipient> findByName(String name) {
        return recipientRepository.findByName(name);
    }

    public List<Recipient> findByNameIgnoreCase(String name) {
        return recipientRepository.findByNameIgnoreCase(name);
    }

    public List<Recipient> searchByName(String namePattern) {
        return recipientRepository.searchByNameContaining(namePattern);
    }

    public List<Recipient> findByCity(String city) {
        return recipientRepository.findByCity(city);
    }

    public List<Recipient> findByPostalCode(String postalCode) {
        return recipientRepository.findByPostalCode(postalCode);
    }

    public List<Recipient> findByCountry(String country) {
        return recipientRepository.findByCountry(country);
    }

    public List<Recipient> findRecipientsWithSpecialInstructions() {
        return recipientRepository.findBySpecialInstructionsIsNotNull();
    }

    public long countAllRecipients() {
        return recipientRepository.count();
    }

    public boolean existsById(Long id) {
        return recipientRepository.existsById(id);
    }


    /**
     * Helper method
     */
    public String getFormattedAddress(Long id) {
        Recipient recipient = recipientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found with ID: " + id));
        return recipient.getFullAddress();
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
