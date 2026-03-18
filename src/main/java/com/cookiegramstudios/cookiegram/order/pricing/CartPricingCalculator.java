package com.cookiegramstudios.cookiegram.order.pricing;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.cart.CartItem;

/**
 * Performs cart pricing math (subtotal, tax, total).
 * Uses BigDecimal internally for better monetary precision.
 * 
 * @author Matthew Samaha
 * @date 2026-03-18
 */
@Component
public class CartPricingCalculator {

    public double calculateSubtotal(Cart cart) {
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return 0.0;
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {
            BigDecimal basePrice = BigDecimal.valueOf(item.getProductType().getBasePrice());
            BigDecimal quantity = BigDecimal.valueOf(item.getItemQty());
            BigDecimal lineTotal = basePrice.multiply(quantity);
            subtotal = subtotal.add(lineTotal);
        }

        return subtotal.doubleValue();
    }

    public double calculateTax(double subtotal, double taxRate) {
        BigDecimal subtotalBd = BigDecimal.valueOf(subtotal);
        BigDecimal taxRateBd = BigDecimal.valueOf(taxRate);
        return subtotalBd.multiply(taxRateBd).doubleValue();
    }

    public double calculateTotal(double subtotal, double tax) {
        BigDecimal subtotalBd = BigDecimal.valueOf(subtotal);
        BigDecimal taxBd = BigDecimal.valueOf(tax);
        return subtotalBd.add(taxBd).doubleValue();
    }
}
