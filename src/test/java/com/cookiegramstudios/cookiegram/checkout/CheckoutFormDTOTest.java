package com.cookiegramstudios.cookiegram.checkout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

import com.cookiegramstudios.cookiegram.order.CheckoutFormDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Unit tests for {@link CheckoutFormDTO} validation constraints.
 * <p>
 * Validates that Bean Validation annotations (@NotBlank, @Email, @Future, @NotNull)
 * behave correctly for all fields in the checkout form DTO.
 * These are plain unit tests — no Spring context is needed.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-17
 * @version 1.0
 */
public class CheckoutFormDTOTest {
	
	private Validator validator;
	 
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
 
    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------
 
    /**
     * Builds a fully valid CheckoutFormDTO with all required fields populated.
     * Tests can mutate specific fields to trigger constraint violations.
     */
    private CheckoutFormDTO buildValidForm() {
        CheckoutFormDTO form = new CheckoutFormDTO();
        form.setRecipientName("Jane Doe");
        form.setRecipientStreet("123 Maple St");
        form.setRecipientCity("Burlington");
        form.setRecipientPostalCode("L7R 1A1");
        form.setRecipientCountry("Canada");
        form.setDeliveryDate(LocalDate.now().plusDays(1));
        form.setDeliveryTimePreference("Morning");
        form.setSenderName("John Doe");
        form.setSenderEmail("john@example.com");
        form.setSenderPhone("905-555-1234"); // optional
        form.setDeliveryInstructions("Leave at door"); // optional
        List<String> messages = new ArrayList<>();
        messages.add("Happy Birthday!");
        form.setCustomMessages(messages);
        return form;
    }
 
    /**
     * Returns the violation messages as a flat set of strings for easy assertion.
     */
    private Set<String> getMessages(Set<ConstraintViolation<CheckoutFormDTO>> violations) {
        Set<String> messages = new java.util.HashSet<>();
        for (ConstraintViolation<CheckoutFormDTO> v : violations) {
            messages.add(v.getMessage());
        }
        return messages;
    }

	
	/**
	 * Valid Form
	 */
	
	
	/**
	 * Default State
	 */
	
	
	/**
	 * No-arg Constructor
	 */
	
	
	/**
	 * Recipient fields
	 */
	// Blank recipient name
	
	
	// Null recipient name
	
	
	// Blank recipientStreet
	
	
	// Blank recipientCity
	
	
	// Blank recipientPostalCode
	
	
	// Blank recipientCountry
	
	
	/**
	 * Delivery Date
	 */
	
	
	/**
	 * Deliver Time
	 */
	
	
	/**
	 * Sender Fiekds
	 */
	
	
	/**
	 * All Optional Fields
	 */
	
	
	/**
	 * Multiple Violations at once
	 */

}
