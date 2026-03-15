package com.cookiegramstudios.cookiegram.cart;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This is the intermediary step in the order process. This will allow
 * customers to add items into their cart and choose when they would like
 * to move forward with the order process. This provides more autonomy and
 * ownership of the decision process from end-to-end for the customer.
 * 
 * @author Kyle Haines
 */
public class Cart {
	LocalDateTime timeCreated;
	List<CartItem> cartItems;
}
