package com.cookiegramstudios.cookiegram.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import com.cookiegramstudios.cookiegram.order.dto.OrderPricingDTO;
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
	private OrderService orderService;

	@MockitoBean
	private PricingService pricingService;

	@MockitoBean
	private UserService userService;

	private Product buildProduct(Long id, double price) {
		Product p = new Product();
		p.setId(id);
		p.setBaseName("Cookie");
		p.setBasePrice(price);
		return p;
	}

	private Cart buildCart(Product product, int qty) {
		Cart cart = new Cart();
		cart.getCartItems().add(new CartItem(product, qty));
		return cart;
	}

	private OrderPricingDTO mockPricing(String sub, String tax, String total) {
		OrderPricingDTO dto = new OrderPricingDTO();
		dto.setFormattedSubtotal(sub);
		dto.setFormattedTax(tax);
		dto.setFormattedTotal(total);
		dto.setTotal(Double.parseDouble(total));
		return dto;
	}

	private String tomorrow() {
		return LocalDate.now().plusDays(1).toString();
	}

	@Test
	void getCheckout_nullCart_redirects() throws Exception {
		mockMvc.perform(get("/order/checkout")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/order/")).andExpect(flash().attributeExists("errorMessage"));
	}

	@Test
	void getCheckout_emptyCart_redirects() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", new Cart());

		mockMvc.perform(get("/order/checkout").session(session)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/order/"));
	}

	@Test
	void getCheckout_validCart_returnsView() throws Exception {
		Product p = buildProduct(1L, 10.0);
		Cart cart = buildCart(p, 2);

		when(pricingService.getOrderPricing(any())).thenReturn(mockPricing("20.00", "2.60", "22.60"));

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(get("/order/checkout").session(session).with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("checkout")).andExpect(model().attributeExists("cartItems"))
				.andExpect(model().attribute("subtotal", "20.00")).andExpect(model().attribute("tax", "2.60"))
				.andExpect(model().attribute("total", "22.60")).andExpect(model().attributeExists("checkoutForm"));
	}

	@Test
	void submitCheckout_valid_redirectsToConfirmation() throws Exception {
		Product p = buildProduct(1L, 10.0);
		Cart cart = buildCart(p, 1);

		when(pricingService.getOrderPricing(any())).thenReturn(mockPricing("10.00", "1.30", "11.30"));

		when(orderService.createOrder(any())).thenReturn(new Order()); // minimal stub

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(post("/order/checkout").session(session).with(csrf()).param("recipientName", "Jane")
				.param("recipientStreet", "123 St").param("recipientCity", "City")
				.param("recipientPostalCode", "A1A1A1").param("recipientCountry", "Canada")
				.param("deliveryDate", tomorrow()).param("deliveryTimePreference", "Morning")
				.param("senderName", "John").param("senderEmail", "john@test.com").param("customMessages[0]", "Hi"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/order/confirmation"));
	}

	@Test
	void submitCheckout_validationError_returnsCheckout() throws Exception {
		Product p = buildProduct(1L, 10.0);
		Cart cart = buildCart(p, 1);

		when(pricingService.getOrderPricing(any())).thenReturn(mockPricing("10.00", "1.30", "11.30"));

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", cart);

		mockMvc.perform(post("/order/checkout").session(session).with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("checkout")).andExpect(model().attributeExists("cartItems"))
				.andExpect(model().attribute("subtotal", "10.00")).andExpect(model().attribute("tax", "1.30"))
				.andExpect(model().attribute("total", "11.30"));
	}

	@Test
	void submitCheckout_emptyCart_redirects() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart", new Cart());

		mockMvc.perform(post("/order/checkout").session(session).with(csrf()).param("recipientName", "Jane")
				.param("recipientStreet", "123 St").param("recipientCity", "City")
				.param("recipientPostalCode", "A1A1A1").param("recipientCountry", "Canada")
				.param("deliveryDate", tomorrow()).param("deliveryTimePreference", "Morning")
				.param("senderName", "John").param("senderEmail", "john@test.com"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/order/"));
	}

	@Test
	void confirmation_noOrder_redirects() throws Exception {
		mockMvc.perform(get("/order/confirmation")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/order/"));
	}

}
