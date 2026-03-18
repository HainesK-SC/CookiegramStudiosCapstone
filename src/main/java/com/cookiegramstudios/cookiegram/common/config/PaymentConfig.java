package com.cookiegramstudios.cookiegram.common.config;

import java.util.Map;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for payment-related settings
 * 
 * 
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
@Configuration
public class PaymentConfig {
	
	private Map<String, Double> taxRates; 
	private double defaultTaxRate;
    private String defaultProvince;
    private String currency;
    private OrderNumberSettings orderNumberSettings;
    private PricingSettings pricing;

}
