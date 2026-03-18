package com.cookiegramstudios.cookiegram.recipient;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cookiegramstudios.cookiegram.order.dto.CheckoutFormDTO;
import com.cookiegramstudios.cookiegram.utils.NameParser;

import jakarta.transaction.Transactional;

/**
 * Service layer for recipient-related business operations.
 * <p>
 * Provides application-facing methods for managing recipients and acts as an
 * abstraction between controllers and {@link RecipientRepository}.
 * Handles recipient creation, retrieval, updates, and search operations.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-17
 * @version 2.1
 */
@Service
public class RecipientService {

	private final RecipientRepository recipientRepository;
    private final RecipientValidationService recipientValidationService;

    public RecipientService(RecipientRepository recipientRepository,
                            RecipientValidationService recipientValidationService) {
        this.recipientRepository = recipientRepository;
        this.recipientValidationService = recipientValidationService;
    }

    @Transactional
    public Recipient createRecipientFromCheckoutForm(CheckoutFormDTO form) {
        Recipient recipient = new Recipient();

        // Set full name
        recipient.setName(form.getRecipientName());

        // Parse recipient name using utility class
        String[] nameParts = NameParser.parseFullName(form.getRecipientName());
        recipient.setFirstName(nameParts[0]);
        recipient.setLastName(nameParts[1]);

        // Set address fields
        recipient.setStreet(form.getRecipientStreet());
        recipient.setCity(form.getRecipientCity());
        recipient.setPostalCode(normalizePostalCode(form.getRecipientPostalCode()));
        recipient.setCountry(form.getRecipientCountry());

        // Set special instructions if provided
        recipient.setSpecialInstructions(form.getDeliveryInstructions());

        return recipientRepository.save(recipient);
    }

    private String normalizePostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            return postalCode;
        }

        // Remove all spaces and convert to uppercase
        String cleaned = postalCode.replaceAll("\\s+", "").toUpperCase();

        // If it's a valid 6-character Canadian postal code, add space in middle
        if (cleaned.length() == 6 && cleaned.matches("[A-Z]\\d[A-Z]\\d[A-Z]\\d")) {
            return cleaned.substring(0, 3) + " " + cleaned.substring(3);
        }

        // Return as-is if not standard format (for international addresses)
        return postalCode.toUpperCase().trim();
    }

    @Transactional
    public Recipient createRecipient(Recipient recipient) {
        recipientValidationService.validateRecipient(recipient);
        return recipientRepository.save(recipient);
    }

    @Transactional
    public Recipient updateRecipient(Recipient recipient) {
        if (recipient.getId() == null || !recipientRepository.existsById(recipient.getId())) {
            throw new IllegalArgumentException("Recipient does not exist with ID: " + recipient.getId());
        }
        recipientValidationService.validateRecipient(recipient);
        return recipientRepository.save(recipient);
    }

    @Transactional
    public void deleteRecipient(Long id) {
        if (!recipientRepository.existsById(id)) {
            throw new IllegalArgumentException("Recipient not found with ID: " + id);
        }
        recipientRepository.deleteById(id);
    }

    public Optional<Recipient> findById(Long id) {
        return recipientRepository.findById(id);
    }

    public List<Recipient> findAllRecipients() {
        return recipientRepository.findAll();
    }

    public List<Recipient> findByName(String name) {
        return recipientRepository.findByFirstName(name);
    }

    public List<Recipient> findByNameIgnoreCase(String name) {
        return recipientRepository.findByFirstNameIgnoreCase(name);
    }

    public List<Recipient> searchByName(String namePattern) {
        return recipientRepository.searchByFirstNameContaining(namePattern);
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

    public String getFormattedAddress(Long id) {
        Recipient recipient = recipientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found with ID: " + id));
        return recipient.getFullAddress();
    }
}
