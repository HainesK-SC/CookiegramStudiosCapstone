package com.cookiegramstudios.cookiegram.order.pricing;

import org.springframework.stereotype.Component;

/**
 * Formats monetary amounts for display.
 * 
 * @author Matthew Samaha
 * @date 2026-03-18
 */
@Component
public class MoneyFormatter {

    public String formatCurrency(double amount) {
        return String.format("%.2f", amount);
    }
}