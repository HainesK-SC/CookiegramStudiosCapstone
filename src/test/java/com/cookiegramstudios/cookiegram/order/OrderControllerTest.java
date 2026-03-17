package com.cookiegramstudios.cookiegram.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cookiegramstudios.cookiegram.cart.CartService;
import com.cookiegramstudios.cookiegram.product.ProductRepository;

/**
 * Unit tests for {@link OrderController}.
 * <p>
 * Covers the GET /order/checkout and POST /order/checkout endpoints, including
 * cart validation, tax calculation, form validation, and session management.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-17
 * @version 1.0
 */
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductRepository productRepository;

	@MockitoBean
	private CartService cartService;

	/**
	 * GET /order/checkout -- null cart
	 */

	/**
	 * GET /order/checkout -- empty cart
	 */

	/**
	 * GET /order/checkout -- valid cart
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

	/**
	 * When Fails
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
	 * If a valid form is submitted - no cart in session - redirect to /order/ with
	 * a flash error
	 */

}
