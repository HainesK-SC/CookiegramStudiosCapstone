package com.cookiegramstudios.cookiegram.checkout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cookiegramstudios.cookiegram.order.CheckoutFormDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Unit tests for {@link CheckoutFormDTO} validation constraints.
 * <p>
 * Validates that Bean Validation annotations
 * (@NotBlank, @Email, @Future, @NotNull) behave correctly for all fields in the
 * checkout form DTO. These are plain unit tests — no Spring context is needed.
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
	@Test
	void validForm_noViolations() {
		CheckoutFormDTO form = buildValidForm();
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertTrue(violations.isEmpty(), "Expected no violations for a valid form");
	}

	/**
	 * Default State
	 */
	@Test
	void defaultConstructor_customMessagesInitializedAsEmptyList() {
		CheckoutFormDTO form = new CheckoutFormDTO();
		assertNotNull(form.getCustomMessages(), "customMessages should not be null");
		assertTrue(form.getCustomMessages().isEmpty(), "customMessages should be empty by default");
	}

	/**
	 * No-arg Constructor
	 */
	@Test
	void defaultConstructor_recipientCountryDefaultsToCanada() {
		CheckoutFormDTO form = new CheckoutFormDTO();
		assertEquals("Canada", form.getRecipientCountry());
	}

	/**
	 * A blank recipientName should produce a @NotBlank violation.
	 */
	@Test
	void blankRecipientName_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setRecipientName("");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Recipient name is required"));
	}

	/**
	 * A null recipientName should produce a @NotBlank violation.
	 */
	@Test
	void nullRecipientName_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setRecipientName(null);
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Recipient name is required"));
	}

	/**
	 * A blank recipientStreet should produce a @NotBlank violation.
	 */
	@Test
	void blankRecipientStreet_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setRecipientStreet("");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Street address is required"));
	}

	/**
	 * A blank recipientCity should produce a @NotBlank violation.
	 */
	@Test
	void blankRecipientCity_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setRecipientCity("");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("City is required"));
	}

	/**
	 * A blank recipientPostalCode should produce a @NotBlank violation.
	 */
	@Test
	void blankRecipientPostalCode_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setRecipientPostalCode("");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Postal code is required"));
	}

	/**
	 * A blank recipientCountry should produce a @NotBlank violation.
	 */
	@Test
	void blankRecipientCountry_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setRecipientCountry("");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Country is required"));
	}

	/**
	 * A null deliveryDate should produce a @NotNull violation.
	 */
	@Test
	void nullDeliveryDate_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setDeliveryDate(null);
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Delivery date is required"));
	}

	/**
	 * A delivery date in the past should produce a @Future violation.
	 */
	@Test
	void pastDeliveryDate_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setDeliveryDate(LocalDate.of(2020, 1, 1));
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Delivery date must be in the future"));
	}

	/**
	 * Today's date should fail @Future (future means strictly after today).
	 */
	@Test
	void todayDeliveryDate_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setDeliveryDate(LocalDate.now());
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Delivery date must be in the future"));
	}

	/**
	 * Tomorrow's date should pass @Future with no violation.
	 */
	@Test
	void tomorrowDeliveryDate_noViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setDeliveryDate(LocalDate.now().plusDays(1));
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertTrue(violations.isEmpty());
	}

	/**
	 * A blank deliveryTimePreference should produce a @NotBlank violation.
	 */
	@Test
	void blankDeliveryTimePreference_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setDeliveryTimePreference("");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Delivery time preference is required"));
	}

	/**
	 * A blank senderName should produce a @NotBlank violation.
	 */
	@Test
	void blankSenderName_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setSenderName("");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Your name is required"));
	}

	/**
	 * A blank senderEmail should produce a @NotBlank violation.
	 */
	@Test
	void blankSenderEmail_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setSenderEmail("");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Email is required"));
	}

	/**
	 * An invalid email format should produce an @Email violation.
	 */
	@Test
	void invalidEmailFormat_producesViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setSenderEmail("not-a-valid-email");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertFalse(violations.isEmpty());
		assertTrue(getMessages(violations).contains("Valid email is required"));
	}

	/**
	 * A valid email format should produce no @Email violation.
	 */
	@Test
	void validEmailFormat_noViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setSenderEmail("test.user+tag@example.co.uk");
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertTrue(violations.isEmpty());
	}

	/**
	 * senderPhone is optional — null should produce no violation.
	 */
	@Test
	void nullSenderPhone_noViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setSenderPhone(null);
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertTrue(violations.isEmpty());
	}

	/**
	 * deliveryInstructions is optional — null should produce no violation.
	 */
	@Test
	void nullDeliveryInstructions_noViolation() {
		CheckoutFormDTO form = buildValidForm();
		form.setDeliveryInstructions(null);
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		assertTrue(violations.isEmpty());
	}

	/**
	 * An entirely empty form should produce violations for every required field.
	 */
	@Test
	void emptyForm_producesMultipleViolations() {
		CheckoutFormDTO form = new CheckoutFormDTO();
		// All required fields are null/blank — only deliveryDate is @NotNull, rest are
		// @NotBlank
		Set<ConstraintViolation<CheckoutFormDTO>> violations = validator.validate(form);
		// Should have violations for: recipientName, recipientStreet, recipientCity,
		// recipientPostalCode, deliveryDate, deliveryTimePreference,
		// senderName, senderEmail
		assertTrue(violations.size() >= 8,
				"Expected at least 9 violations for a completely empty form, got: " + violations.size()); // chanted to
																											// 8 because
																											// recipientCountry
																											// defaults
																											// to
																											// "Canada"
																											// and won't
																											// violate
																											// @NotBlank
	}

}
