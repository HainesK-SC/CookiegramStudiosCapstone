package com.cookiegramstudios.cookiegram.cart;

import java.util.Optional;

import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;

/**
 * Service class that will interact with the CartController to
 * perform various logical tasks related to a Cart object.
 * 
 * @author Kyle Haines
 */
public class CartService {
	
	private final ProductRepository productRepository;
	
	public CartService(ProductRepository prodRepo) {
		this.productRepository = prodRepo;
	}
	
	public void addToCart(Cart cart, Long productId, int quantity) {
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
	}
}
