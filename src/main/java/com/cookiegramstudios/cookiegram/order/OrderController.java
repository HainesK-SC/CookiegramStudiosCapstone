package com.cookiegramstudios.cookiegram.order;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.cart.CartItem;
import com.cookiegramstudios.cookiegram.cart.CartService;
import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;

import jakarta.servlet.http.HttpSession;

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
 * @date 2026-03-13
 * @version 1.1
 */
@Controller
public class OrderController {
	
	private final ProductRepository productRepository;
	private final CartService cartService;
	
	public OrderController(ProductRepository prodRepo, CartService cartServ) {
		this.productRepository = prodRepo;
		this.cartService = cartServ;
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
		if(cart == null) {
			cart = new Cart();
		}
		
		cartService.addToCart(cart, productId, quantity);
		
		session.setAttribute("cart", cart);
		
		return "redirect:/order/cart";
	}
	
	@GetMapping("/order/cart")
	public String cart(HttpSession session, Model model) {
		Cart cart = (Cart) session.getAttribute("cart");
		List<CartItem> cartItems = cart.getCartItems();
		model.addAttribute("cartItems", cartItems);
		return "cart";
	}
	
	@PostMapping("/order/cart/clear")
	public String clearCart(HttpSession session) {
		session.removeAttribute("cart");
		return "cart";
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
	
}
