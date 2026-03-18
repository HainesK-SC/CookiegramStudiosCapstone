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

	

	
}
	

