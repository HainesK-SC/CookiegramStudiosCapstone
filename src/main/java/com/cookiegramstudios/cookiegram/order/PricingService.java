package com.cookiegramstudios.cookiegram.order;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.cart.CartItem;
import com.cookiegramstudios.cookiegram.common.config.PaymentConfig;

/**
 * Service for pricing and tax calculations.
 * <p>
 * Centralizes all pricing logic for orders, including subtotal calculation,
 * tax computation, and total amount determination. Uses {@link PaymentConfig}
 * for tax rates and pricing rules, ensuring all calculations are consistent
 * and easily configurable.
 * </p>
 * <p>
 * <b>Key Responsibilities:</b>
 * <ul>
 * <li>Calculate cart subtotals</li>
 * <li>Apply province-specific tax rates</li>
 * <li>Format currency amounts for display</li>
 * <li>Create comprehensive pricing DTOs</li>
 * </ul>
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
public class PricingService {

	private final PaymentConfig paymentConfig;

	public PricingService(PaymentConfig paymentConfig) {
		this.paymentConfig = paymentConfig;
	}

	public double calculateSubtotal(Cart cart) {
		if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
			return 0.0;
		}

		double subtotal = 0.0;
		for (CartItem item : cart.getCartItems()) {
			double lineTotal = item.getProductType().getBasePrice() * item.getItemQty();
			subtotal += lineTotal;
		}

		return subtotal;
	}

	public double calculateTax(double subtotal, String province) {
		double taxRate = getTaxRate(province);
		return subtotal * taxRate;
	}

	public double calculateTotal(double subtotal, double tax) {
		return subtotal + tax;
	}

	public double getTaxRate(String province) {
		if (province == null || province.trim().isEmpty()) {
			return paymentConfig.getDefaultTaxRate();
		}
		return paymentConfig.getTaxRateForProvince(province.toUpperCase());
	}

	public String formatCurrency(double amount) {
		return String.format("%.2f", amount);
	}

	public OrderPricingDTO getOrderPricing(Cart cart, String province) {
		// Use default province if none provided
		String effectiveProvince = (province == null || province.trim().isEmpty()) ? paymentConfig.getDefaultProvince()
				: province.toUpperCase();

		// Calculate amounts
		double subtotal = calculateSubtotal(cart);
		double taxRate = getTaxRate(effectiveProvince);
		double tax = calculateTax(subtotal, effectiveProvince);
		double total = calculateTotal(subtotal, tax);

		// Create and populate DTO
		OrderPricingDTO pricing = new OrderPricingDTO();
		pricing.setSubtotal(subtotal);
		pricing.setTax(tax);
		pricing.setTaxRate(taxRate);
		pricing.setTotal(total);
		pricing.setFormattedSubtotal(formatCurrency(subtotal));
		pricing.setFormattedTax(formatCurrency(tax));
		pricing.setFormattedTotal(formatCurrency(total));
		pricing.setProvince(effectiveProvince);
		pricing.setCurrency(paymentConfig.getCurrency());

		return pricing;
	}

	public OrderPricingDTO getOrderPricing(Cart cart) {
		return getOrderPricing(cart, null);
	}

}
