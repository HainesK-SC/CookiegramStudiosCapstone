package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CheckoutFormDTO {

	// Recipient Information
	@NotBlank(message = "Recipient name is required")
	private String recipientName;

	@NotBlank(message = "Street address is required")
	private String recipientStreet;

	@NotBlank(message = "City is required")
	private String recipientCity;

	@NotBlank(message = "Postal code is required")
	private String recipientPostalCode;

	@NotBlank(message = "Country is required")
	private String recipientCountry = "Canada";

	// Delivery Details
	@NotNull(message = "Delivery date is required")
	@Future(message = "Delivery date must be in the future")
	private LocalDate deliveryDate;

	@NotBlank(message = "Delivery time preference is required")
	private String deliveryTimePreference;

	// Optional delivery instructions for recipient address
	private String deliveryInstructions;

	// Sender/Customer Information
	@NotBlank(message = "Your name is required")
	private String senderName;

	@Email(message = "Valid email is required")
	@NotBlank(message = "Email is required")
	private String senderEmail;

	private String senderPhone; // Optional

	// Custom Messages (parallel to cart items)
	// Index matches cart item index
	private List<String> customMessages = new ArrayList<>();

	// Constructors
	public CheckoutFormDTO() {
	}

	public CheckoutFormDTO(String recipientName, String recipientStreet, String recipientCity,
			String recipientPostalCode, String recipientCountry, LocalDate deliveryDate, String deliveryTimePreference,
			String deliveryInstructions, String senderName, String senderEmail, String senderPhone,
			List<String> customMessages) {
		this.recipientName = recipientName;
		this.recipientStreet = recipientStreet;
		this.recipientCity = recipientCity;
		this.recipientPostalCode = recipientPostalCode;
		this.recipientCountry = recipientCountry;
		this.deliveryDate = deliveryDate;
		this.deliveryTimePreference = deliveryTimePreference;
		this.deliveryInstructions = deliveryInstructions;
		this.senderName = senderName;
		this.senderEmail = senderEmail;
		this.senderPhone = senderPhone;
		this.customMessages = customMessages != null ? customMessages : new ArrayList<>();
	}

}
