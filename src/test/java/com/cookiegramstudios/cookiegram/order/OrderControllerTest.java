package com.cookiegramstudios.cookiegram.order;

public class OrderControllerTest {
	
	/**
	 * GET /order/checkout -- null cart
	 */
	
	
	/**
	 * GET /order/checkout -- empty cart
	 */
	
	
	/**
	 * GET /order/checkout -- valid  cart
	 */
	
	
	/**
	 * Verifies Ontario HST (13%)
	 */
	
	
	/**
	 * Verifies checkoutForm pre-init
	 */
	
	
	/**
	 * POST /order/checkout - valid form + valid cart
	 */
	
	
	/**
	 * After submission -> session should contain checkoutData
	 */
	
	
	/**
	 * POST /order/checkout - validation errors
	 */
	
	
	/** When Fails
	 * 
	 */
	
	
	/**
	 * Past delivery date should fail - @Future constraint 
	 */
	
	
	/**
	 * Invalid email should fail - @Email constraint
	 */
	
	
	/**
	 * POST /order/checkout - valid form but empty/null cart
	 */
	
	
	/**
	 * If a valid form is submitted - no cart in session - redirect to /order/ with a flash error
	 */


}
