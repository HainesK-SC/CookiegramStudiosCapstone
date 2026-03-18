package com.cookiegramstudios.cookiegram.order;

import java.util.List;

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
import com.cookiegramstudios.cookiegram.cart.CartValidator;
import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;
import com.cookiegramstudios.cookiegram.utils.SessionHelper;

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
	 
	        Cart cart = SessionHelper.getOrCreateCart(session);
	        cartService.addToCart(cart, productId, quantity);
	        SessionHelper.setCart(session, cart);
	 
	        return "redirect:/order/cart";
	    }
	    
	    @GetMapping("/order/cart")
	    public String cart(HttpSession session, Model model) {
	        Cart cart = SessionHelper.getOrCreateCart(session);
	        List<CartItem> cartItems = cart.getCartItems();
	        model.addAttribute("cartItems", cartItems);
	        return "cart";
	    }
	    
	    @PostMapping("/order/cart/clear")
	    public String clearCart(HttpSession session) {
	        SessionHelper.clearCart(session);
	        return "redirect:/order/cart";
	    }
	    
	    @GetMapping("/order/checkout")
	    public String getCheckout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
	    	
	        Cart cart = SessionHelper.getCart(session);
	        
	        if (CartValidator.isCartEmpty(cart)) {
	            redirectAttributes.addFlashAttribute("errorMessage", CartValidator.getEmptyCartMessage());
	            return "redirect:/order/";
	        }
	 
	        OrderPricingDTO pricing = pricingService.getOrderPricing(cart);
	 
	        CheckoutFormDTO checkoutForm = new CheckoutFormDTO();
	        for (int i = 0; i < cart.getCartItems().size(); i++) {
	            checkoutForm.getCustomMessages().add("");
	        }
	 
	        model.addAttribute("cartItems", cart.getCartItems());
	        model.addAttribute("subtotal", pricing.getFormattedSubtotal());
	        model.addAttribute("tax", pricing.getFormattedTax());
	        model.addAttribute("total", pricing.getFormattedTotal());
	        model.addAttribute("checkoutForm", checkoutForm);
	 
	        return "checkout";
	    }
	    
	    @PostMapping("/order/checkout")
	    public String submitCheckout(
	            @Valid @ModelAttribute("checkoutForm") CheckoutFormDTO checkoutForm,
	            BindingResult result,
	            HttpSession session,
	            Model model,
	            RedirectAttributes redirectAttributes) {
	 
	        if (result.hasErrors()) {
	            return handleCheckoutValidationError(session, model, redirectAttributes);
	        }
	 
	        Cart cart = SessionHelper.getCart(session);
	        
	        if (CartValidator.isCartEmpty(cart)) {
	            redirectAttributes.addFlashAttribute("errorMessage", CartValidator.getEmptyCartMessage());
	            return "redirect:/order/";
	        }
	 
	        OrderPricingDTO pricing = pricingService.getOrderPricing(cart);
	        OrderCreationRequest request = new OrderCreationRequest(cart, checkoutForm, pricing.getTotal());
	        Order savedOrder = orderService.createOrder(request);
	 
	        SessionHelper.setConfirmedOrder(session, savedOrder);
	        return "redirect:/order/confirmation";
	    }
	    
	    @GetMapping("/order/confirmation")
	    public String getConfirmation(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
	        // Retrieve and validate confirmed order
	        Order confirmedOrder = SessionHelper.getConfirmedOrder(session);
	 
	        if (confirmedOrder == null) {
	            redirectAttributes.addFlashAttribute("errorMessage", "No order found. Please start a new order.");
	            return "redirect:/order/";
	        }
	 
	        // Add order details to model
	        model.addAttribute("orderNumber", confirmedOrder.getOrderNumber());
	        model.addAttribute("recipient", confirmedOrder.getRecipientUser());
	        model.addAttribute("deliveryDate", confirmedOrder.getDeliveryDate());
	 
	        // Clean up session
	        SessionHelper.clearOrderSession(session);
	 
	        return "confirmation";
	    }
	    
	    
	    private String handleCheckoutValidationError(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
	        Cart cart = SessionHelper.getCart(session);
	 
	        if (CartValidator.isCartEmpty(cart)) {
	            redirectAttributes.addFlashAttribute("errorMessage", CartValidator.getEmptyCartMessage());
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
	
}
	

