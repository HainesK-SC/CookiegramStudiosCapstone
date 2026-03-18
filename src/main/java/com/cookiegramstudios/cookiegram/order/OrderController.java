package com.cookiegramstudios.cookiegram.order;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.cart.CartItem;
import com.cookiegramstudios.cookiegram.cart.CartService;
import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;
import com.cookiegramstudios.cookiegram.utils.SessionHelper;

import jakarta.servlet.http.HttpSession;

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
	    
	    
	    

	

	
}
	

