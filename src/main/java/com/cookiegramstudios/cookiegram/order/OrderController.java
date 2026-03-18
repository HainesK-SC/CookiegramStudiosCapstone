package com.cookiegramstudios.cookiegram.order;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.cart.CartItem;
import com.cookiegramstudios.cookiegram.cart.CartService;
import com.cookiegramstudios.cookiegram.customer.Customer;
import com.cookiegramstudios.cookiegram.customer.CustomerRepository;
import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;
import com.cookiegramstudios.cookiegram.recipient.Recipient;
import com.cookiegramstudios.cookiegram.recipient.RecipientRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * Controller for order-related web endpoints.
 * <p>
 * Serves as the entry point for order workflow pages and interactions,
 * including order creation, review, and confirmation flows.
 * Endpoint methods will be added incrementally as the order feature set is implemented.
 * </p>
 * <p>
 * <b>Base Path:</b> {@code /order}
 * </p>
 *
 * @author Matthew Samaha
 * @auhor Kyle Haines
 * @date 2026-02-23
 * @date 2026-03-18 
 * @version 1.5
 */
@Controller
public class OrderController {

	private final ProductRepository productRepository;
	private final CartService cartService;
	
	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final RecipientRepository recipientRepository;

	public OrderController(ProductRepository productRepository, CartService cartService, OrderRepository orderRepository, CustomerRepository customerRepository, RecipientRepository recipientRepository) {
		this.productRepository = productRepository;
		this.cartService = cartService;
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.recipientRepository = recipientRepository;
	}

	@GetMapping("/order")
	public String order(Model model) {
		List<Product> currentProducts = productRepository.findAll();
		model.addAttribute("currentProducts", currentProducts);
		return "order";
	}

	@PostMapping("/order/cart/add")
	public String addToCart(
			@RequestParam("productId") Long productId,
			@RequestParam("productQuantity") int quantity,
			HttpSession session) {

		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
		}

		cartService.addToCart(cart, productId, quantity);

		session.setAttribute("cart", cart);

		return "redirect:/order/cart";
	}

	@GetMapping("/order/cart")
	public String cart(HttpSession session, Model model) {
		Cart cart = (Cart) session.getAttribute("cart");

		if (cart == null) {
			cart = new Cart();
		}

		List<CartItem> cartItems = cart.getCartItems();
		model.addAttribute("cartItems", cartItems);
		return "cart";
	}

	@PostMapping("/order/cart/clear")
	public String clearCart(HttpSession session) {
		session.removeAttribute("cart");
		return "redirect:/order/cart";
	}

	@GetMapping("/order/checkout")
	public String getCheckout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		// 1. retrieve cart from session
		Cart cart = (Cart) session.getAttribute("cart");

		// 2. validate cart exsists 
		if (cart == null || cart.getCartItems().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
			return "redirect:/order/"; // Redirect to order page or cart page as appropriate
		}

		// 3. calculate total price of cart items
		double subtotal = 0.0;
		for (CartItem i : cart.getCartItems()) {
			double lineTotal = i.getProductType().getBasePrice() * i.getItemQty();
			subtotal += lineTotal;
		}

		// Ontario HST
		double tax = subtotal * 0.13;
		double total = subtotal + tax;


		// 4. prepare checkout form with pre-init message list
		CheckoutFormDTO checkoutForm = new CheckoutFormDTO();
		for (int i = 0; i < cart.getCartItems().size(); i++) { // For each cart item, add a placeholder for a custom message

			checkoutForm.getCustomMessages().add(""); // Add empty string as placeholder for custom message

		}

		// 5. add attribute to model
		model.addAttribute("cartItems", cart.getCartItems());
		model.addAttribute("subtotal", String.format("%.2f", subtotal));
		model.addAttribute("tax", String.format("%.2f", tax));
		model.addAttribute("total", String.format("%.2f", total));
		model.addAttribute("checkoutForm", checkoutForm);

		// 6. return checkout template
		return "checkout";
	}
 
	/**
	 * Refactred checkout submission handle
	 * @param checkoutForm
	 * @param result
	 * @param session
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("/order/checkout")
	public String submitCheckout(
			@Valid @ModelAttribute("checkoutForm") CheckoutFormDTO checkoutForm,
			BindingResult result,
			HttpSession session,
			Model model,
			RedirectAttributes redirectAttributes) {

		// 1. if validation errors, return to checkout page with errors
		if (result.hasErrors()) {
			Cart cart = (Cart) session.getAttribute("cart");

			if (cart == null || cart.getCartItems().isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
				return "redirect:/order/";
			}

			double subtotal = 0.0;
			for (CartItem item : cart.getCartItems()) {
				subtotal += item.getProductType().getBasePrice() * item.getItemQty();
			}
			double tax = subtotal * 0.13;
			double total = subtotal + tax;

			model.addAttribute("cartItems", cart.getCartItems());
			model.addAttribute("subtotal", String.format("%.2f", subtotal));
			model.addAttribute("tax", String.format("%.2f", tax));
			model.addAttribute("total", String.format("%.2f", total));

			return "checkout";
		}

		// 2. retrieve cart from session
		Cart cart = (Cart) session.getAttribute("cart");

		// 3. final cart validation (check if cart is empty or null)
		if (cart == null || cart.getCartItems().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
			return "redirect:/order/";
		}

		// 4. CREATE ORDER - NEW LOGIC STARTS HERE
		
		// 4a. Create or find customer
		Customer customer = createOrFindCustomer(checkoutForm);
		
		// 4b. Create recipient
		Recipient recipient = createRecipient(checkoutForm);
		
		// 4c. Calculate total price
		double totalPrice = calculateTotalPrice(cart);
		
		// 4d. Build order notes
		String notes = buildNotesFromCheckoutForm(checkoutForm);
		
		// 4e. Create new Order entity
		Order newOrder = new Order();
		newOrder.setOrderNumber(generateOrderNumber());
		newOrder.setCustomerProfile(customer);
		newOrder.setRecipientUser(recipient);
		newOrder.setStatus(OrderStatus.PLACED);
		newOrder.setDeliveryDate(checkoutForm.getDeliveryDate());
		newOrder.setTotalPrice(totalPrice);
		newOrder.setNotes(notes);
		
		// 4f. Save order to database
		Order savedOrder = orderRepository.save(newOrder);
		
		// 4g. Store order in session for confirmation page
		session.setAttribute("confirmedOrder", savedOrder);
		
		// 5. redirect to confirmation page
		return "redirect:/order/confirmation";
	}
	
	
	
	
	@GetMapping("/order/confirmation")
	public String getConfirmation(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

		// 1. Retrieve completed order from session
		Order confirmedOrder = (Order) session.getAttribute("confirmedOrder");

		// 2. Validate order exists
		if (confirmedOrder == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "No order found. Please start a new order.");
			return "redirect:/order/";
		}

		// 3. Add order details to model
		model.addAttribute("orderNumber", confirmedOrder.getOrderNumber());
		model.addAttribute("recipient", confirmedOrder.getRecipientUser());
		model.addAttribute("deliveryDate", confirmedOrder.getDeliveryDate());

		// 4. Clean up session — order is confirmed, no longer needed
		session.removeAttribute("confirmedOrder");
		session.removeAttribute("checkoutData");
		session.removeAttribute("cart");

		// 5. Return confirmation template
		return "confirmation";
	}
	
	
	/**
	 * Helper Methods 
	 */
	
	// Create a new customer or find existing one based on email
	private Customer createOrFindCustomer(CheckoutFormDTO form) {
		// Check if customer exists by email
		Optional<Customer> existing = customerRepository.findByEmail(form.getSenderEmail());

		if (existing.isPresent()) {
			// Update last order date for existing customer
			Customer customer = existing.get();
			customer.updateLastOrderDate();
			return customerRepository.save(customer);
		} else {
			// Create new customer
			Customer newCustomer = new Customer();
			newCustomer.setEmail(form.getSenderEmail());

			// Parse name (assuming "First Last" format)
			String[] nameParts = form.getSenderName().split(" ", 2);
			newCustomer.setFirstName(nameParts[0]);
			newCustomer.setLastName(nameParts.length > 1 ? nameParts[1] : "");

			return customerRepository.save(newCustomer);
		}
	}
	
	// Creates a new recipient entity from checkout form data
	private Recipient createRecipient(CheckoutFormDTO form) {
	    Recipient recipient = new Recipient();

	    // Set full name
	    recipient.setName(form.getRecipientName());  // ← ADD THIS
	    
	    // Parse recipient name (assuming "First Last" format)
	    String[] nameParts = form.getRecipientName().split(" ", 2);
	    recipient.setFirstName(nameParts[0]);
	    recipient.setLastName(nameParts.length > 1 ? nameParts[1] : "");

	    recipient.setStreet(form.getRecipientStreet());
	    recipient.setCity(form.getRecipientCity());
	    recipient.setPostalCode(form.getRecipientPostalCode());
	    recipient.setCountry(form.getRecipientCountry());
	    recipient.setSpecialInstructions(form.getDeliveryInstructions());

	    return recipientRepository.save(recipient);
	}
	
	// Calculate total price of cart items including tax
	private double calculateTotalPrice(Cart cart) {
		double subtotal = 0.0;
		for (CartItem item : cart.getCartItems()) {
			subtotal += item.getProductType().getBasePrice() * item.getItemQty();
		}
		double tax = subtotal * 0.13;
		return subtotal + tax;
	}
	
	// Generates a unique order number (for simplicity 4-digit random number, in real app would need to ensure uniqueness)
	private int generateOrderNumber() {
		// Simple approach: random 4-digit number
		return (int) (Math.random() * 9000) + 1000;

		// TODO: Consider implementing sequential order numbers based on existing orders
	}
	
	// Builds comprehensive order notes from checkout form data
	private String buildNotesFromCheckoutForm(CheckoutFormDTO form) {
		StringBuilder notes = new StringBuilder();

		notes.append("Delivery Time Preference: ").append(form.getDeliveryTimePreference()).append("\n");

		if (form.getDeliveryInstructions() != null && !form.getDeliveryInstructions().isEmpty()) {
			notes.append("Delivery Instructions: ").append(form.getDeliveryInstructions()).append("\n");
		}

		if (form.getSenderPhone() != null && !form.getSenderPhone().isEmpty()) {
			notes.append("Sender Phone: ").append(form.getSenderPhone()).append("\n");
		}

		// Add custom messages
		if (!form.getCustomMessages().isEmpty()) {
			notes.append("\nCustom Messages:\n");
			for (int i = 0; i < form.getCustomMessages().size(); i++) {
				String message = form.getCustomMessages().get(i);
				if (message != null && !message.trim().isEmpty()) {
					notes.append("  Item ").append(i + 1).append(": ")
							.append(message).append("\n");
				}
			}
		}

		return notes.toString();
	}
	
	

	
	
}
	

