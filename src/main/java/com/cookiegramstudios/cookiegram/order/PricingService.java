package com.cookiegramstudios.cookiegram.order;

import org.springframework.stereotype.Service;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.common.config.PaymentConfig;
import com.cookiegramstudios.cookiegram.order.dto.OrderPricingDTO;
import com.cookiegramstudios.cookiegram.order.pricing.CartPricingCalculator;
import com.cookiegramstudios.cookiegram.order.pricing.MoneyFormatter;
import com.cookiegramstudios.cookiegram.order.pricing.TaxRateResolver;

/**
 * Service for pricing and tax calculations.
 * <p>
 * Orchestrates pricing flow by delegating tax resolution, cart math,
 * and money formatting to focused components.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 2.0
 */
@Service
public class PricingService {

	private final PaymentConfig paymentConfig;
    private final TaxRateResolver taxRateResolver;
    private final CartPricingCalculator cartPricingCalculator;
    private final MoneyFormatter moneyFormatter;

    public PricingService(PaymentConfig paymentConfig,
                          TaxRateResolver taxRateResolver,
                          CartPricingCalculator cartPricingCalculator,
                          MoneyFormatter moneyFormatter) {
        this.paymentConfig = paymentConfig;
        this.taxRateResolver = taxRateResolver;
        this.cartPricingCalculator = cartPricingCalculator;
        this.moneyFormatter = moneyFormatter;
    }

    public double calculateSubtotal(Cart cart) {
        return cartPricingCalculator.calculateSubtotal(cart);
    }

    public double calculateTax(double subtotal, String province) {
        double taxRate = taxRateResolver.resolveTaxRate(province);
        return cartPricingCalculator.calculateTax(subtotal, taxRate);
    }

    public double calculateTotal(double subtotal, double tax) {
        return cartPricingCalculator.calculateTotal(subtotal, tax);
    }

    public double getTaxRate(String province) {
        return taxRateResolver.resolveTaxRate(province);
    }

    public String formatCurrency(double amount) {
        return moneyFormatter.formatCurrency(amount);
    }

    public OrderPricingDTO getOrderPricing(Cart cart, String province) {
        String effectiveProvince = taxRateResolver.resolveProvince(province);

        double subtotal = calculateSubtotal(cart);
        double taxRate = getTaxRate(effectiveProvince);
        double tax = cartPricingCalculator.calculateTax(subtotal, taxRate);
        double total = calculateTotal(subtotal, tax);

        return buildPricingDto(subtotal, tax, taxRate, total, effectiveProvince);
    }

    public OrderPricingDTO getOrderPricing(Cart cart) {
        return getOrderPricing(cart, null);
    }

    private OrderPricingDTO buildPricingDto(double subtotal,
                                            double tax,
                                            double taxRate,
                                            double total,
                                            String province) {
        OrderPricingDTO pricing = new OrderPricingDTO();
        pricing.setSubtotal(subtotal);
        pricing.setTax(tax);
        pricing.setTaxRate(taxRate);
        pricing.setTotal(total);
        pricing.setFormattedSubtotal(formatCurrency(subtotal));
        pricing.setFormattedTax(formatCurrency(tax));
        pricing.setFormattedTotal(formatCurrency(total));
        pricing.setProvince(province);
        pricing.setCurrency(paymentConfig.getCurrency());
        return pricing;
    }
}
