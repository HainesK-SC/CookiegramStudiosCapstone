package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckoutFormDTO {
	
	// Recipient Information
	private String recipientName;
	
	private String recipientSteet;
	
	private String recipientCity;
	
	private String recipientPostalCode;
	
	private String recipientCountry;
	
	
	// Delivery Information
	private LocalDate deliveryDate;
	
	private String deliveryInstructions;
	
	private String deliveryTimePreference;
	
	
	// Sender/Customer Information
	private String senderName;
	
	private String senderEmail;
	
	private String senderPhone; // optional, but can be useful for contact purposes
	
	// custom messages (parralel to cart items)
	// index matches cart item index, so if cart has 3 items, this list should have 3 messages (can be empty strings if no message for that item)
	private List<String> customMessaged = new ArrayList<>(); 
	
	
	public CheckoutFormDTO() {
		// default constructor
	}
		

}
