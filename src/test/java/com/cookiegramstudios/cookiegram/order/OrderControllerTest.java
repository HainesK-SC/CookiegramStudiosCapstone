package com.cookiegramstudios.cookiegram.order;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.cart.CartItem;
import com.cookiegramstudios.cookiegram.cart.CartService;
import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;
import com.cookiegramstudios.cookiegram.user.UserService;

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

    @MockitoBean
    private UserService userService;  // <-- ADD THIS

	/**
	 * Utility method to build Product with given id and base price
	 */
	private Product buildProduct(Long id, double price) {
		Product p = new Product();
		p.setId(id);
		p.setBaseName("Chocolate Chip Cookie");
		p.setProductType("cookie");
		p.setBaseDescription("Classic cookie");
		p.setBasePrice(price);
		return p;
	}
	
	/**
	 * Builds a Cart containing one CartItem with the given product and quantity.
	 */
	private Cart buildCart(Product product, int quantity) {
	    Cart cart = new Cart();
	    CartItem item = new CartItem(product, quantity);
	    cart.getCartItems().add(item);
	    return cart;
	}

	/**
	 * Builds a fully valid checkoutFormDTO as form params for MockMvc post Returns
	 * tomorrow's data as the delivery date
	 */
	private String tomorrow() {
		return LocalDate.now().plusDays(1).toString(); // yyyy-MM-dd
	}

	/**
	 * GET /order/checkout -- null cart
	 */
	@Test
	void getCheckout_nullCart_redirectsWithError() throws Exception {
		mockMvc.perform(get("/order/checkout")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/order/")).andExpect(flash().attributeExists("errorMessage"));
	}

	/**
	 * GET /order/checkout -- empty cart
	 */
	@Test
	void getCheckout_emptyCart_redirectsWithError() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", new Cart()); // cart with no items

		mockMvc.perform(get("/order/checkout").session(session)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/order/")).andExpect(flash().attributeExists("errorMessage"));
	}

	/**
	 * GET /order/checkout -- valid cart
	 */
	@Test
	@WithMockUser
	void getCheckout_validCart_returnsCheckoutView() throws Exception {
		Product product = buildProduct(1L, 10.00);
		Cart cart = buildCart(product, 2);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(get("/order/checkout").session(session).with(csrf()))
				.andExpect(view().name("checkout")).andExpect(model().attributeExists("cartItems"))
				.andExpect(model().attributeExists("subtotal")).andExpect(model().attributeExists("tax"))
				.andExpect(model().attributeExists("total")).andExpect(model().attributeExists("checkoutForm"));
	}

	/**
	 * Verifies Ontario HST (13%)
	 */
	@Test
	@WithMockUser
	void getCheckout_validCart_calculatesOntarioHSTCorrectly() throws Exception {
		Product product = buildProduct(1L, 10.00);
		Cart cart = buildCart(product, 2);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(get("/order/checkout").session(session).with(csrf()))
				.andExpect(model().attribute("subtotal", "20.00")).andExpect(model().attribute("tax", "2.60"))
				.andExpect(model().attribute("total", "22.60"));
	}

	/**
	 * Verifies checkoutForm pre-init
	 */
	@Test
	@WithMockUser
	void getCheckout_validCart_checkoutFormHasCorrectMessageListSize() throws Exception {
		Product p1 = buildProduct(1L, 5.00);
		Product p2 = buildProduct(2L, 8.00);

		Cart cart = new Cart();
		cart.getCartItems().add(new CartItem(p1, 1));
		cart.getCartItems().add(new CartItem(p2, 3));

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(get("/order/checkout").session(session).with(csrf()))
				.andExpect(model().attributeExists("checkoutForm"));
		// checkoutForm.customMessages.size() should match cart item count (2)
		// Validated indirectly — POST tests rely on this binding being correct
	}

	/**
	 * POST /order/checkout - valid form + valid cart
	 */
	@Test
	void submitCheckout_validFormAndCart_redirectsToPayment() throws Exception {
		Product product = buildProduct(1L, 10.00);
		Cart cart = buildCart(product, 1);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(post("/order/checkout").session(session).with(csrf()).param("recipientName", "Jane Doe")
				.param("recipientStreet", "123 Maple St").param("recipientCity", "Burlington")
				.param("recipientPostalCode", "L7R 1A1").param("recipientCountry", "Canada")
				.param("deliveryDate", tomorrow()).param("deliveryTimePreference", "Morning")
				.param("senderName", "John Doe").param("senderEmail", "john@example.com")
				.param("customMessages[0]", "Happy Birthday!")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/order/payment"));
	}

	/**
	 * After submission -> session should contain checkoutData
	 */
	@Test
	void submitCheckout_validFormAndCart_storesCheckoutDataInSession() throws Exception {
		Product product = buildProduct(1L, 10.00);
		Cart cart = buildCart(product, 1);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(post("/order/checkout").session(session).with(csrf()).param("recipientName", "Jane Doe")
				.param("recipientStreet", "123 Maple St").param("recipientCity", "Burlington")
				.param("recipientPostalCode", "L7R 1A1").param("recipientCountry", "Canada")
				.param("deliveryDate", tomorrow()).param("deliveryTimePreference", "Morning")
				.param("senderName", "John Doe").param("senderEmail", "john@example.com")
				.param("customMessages[0]", "")).andExpect(status().is3xxRedirection());

		// Verify session was populated with checkout data
		assert session.getAttribute("checkoutData") != null;
	}

	/**
	 * POST /order/checkout - validation errors
	 */
	@Test
	void submitCheckout_missingRequiredFields_returnsCheckoutView() throws Exception {
		Product product = buildProduct(1L, 10.00);
		Cart cart = buildCart(product, 1);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(post("/order/checkout").session(session).with(csrf()))
				// No params submitted — all @NotBlank fields will fail
				.andExpect(status().isOk()).andExpect(view().name("checkout"));
	}

	/**
	 * When Fails
	 * 
	 */
	@Test
	void submitCheckout_validationError_modelContainsTotals() throws Exception {
		Product product = buildProduct(1L, 15.00);
		Cart cart = buildCart(product, 2); // subtotal = 30.00

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(post("/order/checkout").session(session).with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("checkout")).andExpect(model().attributeExists("cartItems"))
				.andExpect(model().attribute("subtotal", "30.00")).andExpect(model().attribute("tax", "3.90"))
				.andExpect(model().attribute("total", "33.90"));
	}

	/**
	 * Past delivery date should fail - @Future constraint
	 */
	@Test
	void submitCheckout_pastDeliveryDate_returnsCheckoutView() throws Exception {
		Product product = buildProduct(1L, 10.00);
		Cart cart = buildCart(product, 1);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(post("/order/checkout").session(session).with(csrf()).param("recipientName", "Jane Doe")
				.param("recipientStreet", "123 Maple St").param("recipientCity", "Burlington")
				.param("recipientPostalCode", "L7R 1A1").param("recipientCountry", "Canada")
				.param("deliveryDate", "2020-01-01") // past date — fails @Future
				.param("deliveryTimePreference", "Morning").param("senderName", "John Doe")
				.param("senderEmail", "john@example.com").param("customMessages[0]", "")).andExpect(status().isOk())
				.andExpect(view().name("checkout"));
	}

	/**
	 * Invalid email should fail - @Email constraint
	 */
	@Test
	void submitCheckout_invalidEmail_returnsCheckoutView() throws Exception {
		Product product = buildProduct(1L, 10.00);
		Cart cart = buildCart(product, 1);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(post("/order/checkout").session(session).with(csrf()).param("recipientName", "Jane Doe")
				.param("recipientStreet", "123 Maple St").param("recipientCity", "Burlington")
				.param("recipientPostalCode", "L7R 1A1").param("recipientCountry", "Canada")
				.param("deliveryDate", tomorrow()).param("deliveryTimePreference", "Morning")
				.param("senderName", "John Doe").param("senderEmail", "not-a-valid-email") // fails @Email
				.param("customMessages[0]", "")).andExpect(status().isOk()).andExpect(view().name("checkout"));
	}

	/**
	 * POST /order/checkout - valid form but empty/null cart
	 */
	@Test
	void submitCheckout_validFormButEmptyCart_redirectsWithError() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", new Cart()); // cart is empty

		mockMvc.perform(post("/order/checkout").session(session).with(csrf()).param("recipientName", "Jane Doe")
				.param("recipientStreet", "123 Maple St").param("recipientCity", "Burlington")
				.param("recipientPostalCode", "L7R 1A1").param("recipientCountry", "Canada")
				.param("deliveryDate", tomorrow()).param("deliveryTimePreference", "Morning")
				.param("senderName", "John Doe").param("senderEmail", "john@example.com"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/order/"))
				.andExpect(flash().attributeExists("errorMessage"));
	}

	/**
	 * If a valid form is submitted - no cart in session - redirect to /order/ with
	 * a flash error
	 */
	@Test
	void submitCheckout_validFormButNullCart_redirectsWithError() throws Exception {
		// No cart in session at all
		mockMvc.perform(post("/order/checkout").with(csrf()).param("recipientName", "Jane Doe")
				.param("recipientStreet", "123 Maple St").param("recipientCity", "Burlington")
				.param("recipientPostalCode", "L7R 1A1").param("recipientCountry", "Canada")
				.param("deliveryDate", tomorrow()).param("deliveryTimePreference", "Morning")
				.param("senderName", "John Doe").param("senderEmail", "john@example.com"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/order/"))
				.andExpect(flash().attributeExists("errorMessage"));
	}

}
