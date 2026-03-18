package com.cookiegramstudios.cookiegram.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
 
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for payment and pricing settings.
 * <p>
 * Loads payment configuration from application.properties and provides
 * centralized access to tax rates, order number settings, and pricing rules.
 * This enables easy modification of business rules without code changes.
 * </p>
 * <p>
 * Configuration is loaded once at application startup and cached as a Spring bean.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 2.0
 */
@Configuration
@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {

	private Map<String, Double> taxRates = new HashMap<>();
    private double defaultTaxRate = 0.13;
    private String defaultProvince = "ON";
    private String currency = "CAD";
    private OrderNumberSettings orderNumberSettings = new OrderNumberSettings();
    private PricingSettings pricing = new PricingSettings();
 
    /**
     * Retrieves the tax rate for a specific province.
     *
     * @param province two-letter province code (e.g., "ON", "BC")
     * @return tax rate as decimal (e.g., 0.13 for 13%)
     */
    public double getTaxRateForProvince(String province) {
        return taxRates.getOrDefault(province, defaultTaxRate);
    }
 
    // Getters and setters for Spring Boot configuration binding
 
    public Map<String, Double> getTaxRates() {
        return taxRates;
    }
 
    public void setTaxRates(Map<String, Double> taxRates) {
        this.taxRates = taxRates;
    }
 
    public double getDefaultTaxRate() {
        return defaultTaxRate;
    }
 
    public void setDefaultTaxRate(double defaultTaxRate) {
        this.defaultTaxRate = defaultTaxRate;
    }
 
    public String getDefaultProvince() {
        return defaultProvince;
    }
 
    public void setDefaultProvince(String defaultProvince) {
        this.defaultProvince = defaultProvince;
    }
 
    public String getCurrency() {
        return currency;
    }
 
    public void setCurrency(String currency) {
        this.currency = currency;
    }
 
    public OrderNumberSettings getOrderNumberSettings() {
        return orderNumberSettings;
    }
 
    public void setOrderNumberSettings(OrderNumberSettings orderNumberSettings) {
        this.orderNumberSettings = orderNumberSettings;
    }
 
    public PricingSettings getPricing() {
        return pricing;
    }
 
    public void setPricing(PricingSettings pricing) {
        this.pricing = pricing;
    }
 
    /**
     * Order number generation settings.
     */
    public static class OrderNumberSettings {
        private String prefix = "CG";
        private int startRange = 1000;
        private int endRange = 9999;
        private boolean useSequential = true;
 
        public String getPrefix() {
            return prefix;
        }
 
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
 
        public int getStartRange() {
            return startRange;
        }
 
        public void setStartRange(int startRange) {
            this.startRange = startRange;
        }
 
        public int getEndRange() {
            return endRange;
        }
 
        public void setEndRange(int endRange) {
            this.endRange = endRange;
        }
 
        public boolean isUseSequential() {
            return useSequential;
        }
 
        public void setUseSequential(boolean useSequential) {
            this.useSequential = useSequential;
        }
    }
 
    /**
     * General pricing settings.
     */
    public static class PricingSettings {
        private double minimumOrderAmount = 0.0;
        private double deliveryFee = 0.0;
 
        public double getMinimumOrderAmount() {
            return minimumOrderAmount;
        }
 
        public void setMinimumOrderAmount(double minimumOrderAmount) {
            this.minimumOrderAmount = minimumOrderAmount;
        }
 
        public double getDeliveryFee() {
            return deliveryFee;
        }
 
        public void setDeliveryFee(double deliveryFee) {
            this.deliveryFee = deliveryFee;
        }
    }
}
