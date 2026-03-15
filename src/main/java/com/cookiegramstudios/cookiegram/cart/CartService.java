package com.cookiegramstudios.cookiegram.cart;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;

/**
 * Service class that will interact with the CartController to
 * perform various logical tasks related to a Cart object.
 * 
 * @author Kyle Haines
 */

@Service
public class CartService {
	
	private final ProductRepository productRepository;
	private final static Logger logger = LoggerFactory.getLogger(CartService.class);
	
	public CartService(ProductRepository prodRepo) {
		this.productRepository = prodRepo;
	}
	
	public void addToCart(Cart cart, Long productId, int quantity) {
		logger.info("Attempting to add to cart...");
		int initialCartSize = cart.getCartItems().size();
		
		Optional<CartItem> existingItem = cart.getCartItems().stream()
				.filter(item -> item.getProductType().getId().equals(productId))
				.findFirst();
		
		if (existingItem.isPresent() ) {
			existingItem.get().setItemQty(existingItem.get().getItemQty() + quantity);
		}
		else {
			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new RuntimeException("Product not found"));
			
			CartItem newItem = new CartItem(product, quantity);
			cart.getCartItems().add(newItem);
		}
		
		if (cart.getCartItems().size() > initialCartSize) {
			logger.info("Item(s) successfully added to the cart...");
		}
		else {
			logger.info("Error: Item(s) not added to the cart...");
		}
	}
	
	public int getCartSize(Cart cart) {
		List<CartItem> cartItems = cart.getCartItems();
		
		int cartSize = cartItems.size();
		
		return cartSize;
	}
}
