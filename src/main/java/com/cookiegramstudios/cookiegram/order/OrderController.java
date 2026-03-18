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
 * </p>
 *
 * @author Matthew Samaha
 * @author Kyle Haines
 * @date 2026-03-18
 * @version 2.0
 */
@Controller
public class OrderController {

	 private final ProductRepository productRepository;
	    private final CartService cartService;
	    private final OrderService orderService;
	    private final PricingService pricingService;
	 
	    public OrderController(ProductRepository productRepository,
	                          CartService cartService,
	                          OrderService orderService,
	                          PricingService pricingService) {
	        this.productRepository = productRepository;
	        this.cartService = cartService;
	        this.orderService = orderService;
	        this.pricingService = pricingService;
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
        // 1. Retrieve cart from session
        Cart cart = (Cart) session.getAttribute("cart");
 
        // 2. Validate cart exists
        if (cart == null || cart.getCartItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
            return "redirect:/order/";
        }
 
        // 3. Calculate pricing using PricingService
        OrderPricingDTO pricing = pricingService.getOrderPricing(cart);
 
        // 4. Prepare checkout form with pre-initialized message list
        CheckoutFormDTO checkoutForm = new CheckoutFormDTO();
        for (int i = 0; i < cart.getCartItems().size(); i++) {
            checkoutForm.getCustomMessages().add(""); // Add empty string as placeholder
        }
 
        // 5. Add attributes to model
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("subtotal", pricing.getFormattedSubtotal());
        model.addAttribute("tax", pricing.getFormattedTax());
        model.addAttribute("total", pricing.getFormattedTotal());
        model.addAttribute("checkoutForm", checkoutForm);
 
        // 6. Return checkout template
        return "checkout";
    }
 

	@PostMapping("/order/checkout")
    public String submitCheckout(
            @Valid @ModelAttribute("checkoutForm") CheckoutFormDTO checkoutForm,
            BindingResult result,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
 
        // 1. If validation errors, return to checkout page with errors
        if (result.hasErrors()) {
            Cart cart = (Cart) session.getAttribute("cart");
 
            if (cart == null || cart.getCartItems().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
                return "redirect:/order/";
            }
 
            // Recalculate pricing for error state
            OrderPricingDTO pricing = pricingService.getOrderPricing(cart);
 
            model.addAttribute("cartItems", cart.getCartItems());
            model.addAttribute("subtotal", pricing.getFormattedSubtotal());
            model.addAttribute("tax", pricing.getFormattedTax());
            model.addAttribute("total", pricing.getFormattedTotal());
 
            return "checkout";
        }
 
        // 2. Retrieve cart from session
        Cart cart = (Cart) session.getAttribute("cart");
 
        // 3. Final cart validation
        if (cart == null || cart.getCartItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
            return "redirect:/order/";
        }
 
        // 4. Calculate total price
        OrderPricingDTO pricing = pricingService.getOrderPricing(cart);
 
        // 5. Create order creation request
        OrderCreationRequest request = new OrderCreationRequest(
                cart,
                checkoutForm,
                pricing.getTotal()
        );
 
        // 6. Create order using OrderService
        Order savedOrder = orderService.createOrder(request);
 
        // 7. Store order in session for confirmation page
        session.setAttribute("confirmedOrder", savedOrder);
 
        // 8. Redirect to confirmation page
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

	
}
	

