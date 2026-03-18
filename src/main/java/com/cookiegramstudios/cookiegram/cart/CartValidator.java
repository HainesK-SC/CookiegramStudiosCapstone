package com.cookiegramstudios.cookiegram.cart;

/**
 * Utility class for validating shopping cart state.
 * <p>
 * Provides validation methods to ensure carts meet business requirements
 * before proceeding with checkout. Centralizes cart validation logic
 * that was previously scattered across controller methods.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
public class CartValidator {
	
	private CartValidator() {
        throw new UnsupportedOperationException("Utility class - do not instantiate");
    }
	
	
    public static boolean isCartValid(Cart cart) {
        return cart != null && cart.getCartItems() != null && !cart.getCartItems().isEmpty();
    }
 
   
    public static boolean isCartEmpty(Cart cart) {
        return !isCartValid(cart);
    }
 
   
    public static String getEmptyCartMessage() {
        return "Your cart is empty. Please add items before checking out.";
    }
 
  
    public static void validateCartNotEmpty(Cart cart) {
        if (isCartEmpty(cart)) {
            throw new IllegalStateException(getEmptyCartMessage());
        }
    }

    
    public static int getTotalItemCount(Cart cart) {
        if (isCartEmpty(cart)) {
            return 0;
        }
 
        return cart.getCartItems().stream()
                .mapToInt(CartItem::getItemQty)
                .sum();
    }
 

    public static boolean meetsCheckoutRequirements(Cart cart) {
        // For now, just check cart has items
        // Future: Could add minimum order value, product availability checks, etc.
        return isCartValid(cart);
    }
}
