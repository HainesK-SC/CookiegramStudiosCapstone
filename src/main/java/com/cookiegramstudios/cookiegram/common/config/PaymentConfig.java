package com.cookiegramstudios.cookiegram.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Map;

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

	@Bean
	public PaymentConfig paymentConfig() {
		try {
			ObjectMapper mapper = new ObjectMapper();

			// Load JSON configuration from classpath ->
			// src/main/resources/payment-config.json
			ClassPathResource resource = new ClassPathResource("payment-config.json");

			// Parse JSON into Map
			@SuppressWarnings("unchecked")
			Map<String, Object> config = mapper.readValue(resource.getInputStream(), Map.class);

			PaymentConfig paymentConfig = new PaymentConfig();

			// Load tax rates
			@SuppressWarnings("unchecked")
			Map<String, Object> taxRatesRaw = (Map<String, Object>) config.get("taxRates");
			Map<String, Double> taxRates = new java.util.HashMap<>();
			taxRatesRaw.forEach((key, value) -> {
				if (value instanceof Number) {
					taxRates.put(key, ((Number) value).doubleValue());
				}
			});
			paymentConfig.setTaxRates(taxRates);

			// Load defaults
			paymentConfig.setDefaultTaxRate(((Number) config.get("defaultTaxRate")).doubleValue());
			paymentConfig.setDefaultProvince((String) config.get("defaultProvince"));
			paymentConfig.setCurrency((String) config.get("currency"));

			// Load order number settings
			@SuppressWarnings("unchecked")
			Map<String, Object> orderSettings = (Map<String, Object>) config.get("orderNumberSettings");
			OrderNumberSettings orderNumberSettings = new OrderNumberSettings();
			orderNumberSettings.setPrefix((String) orderSettings.get("prefix"));
			orderNumberSettings.setStartRange(((Number) orderSettings.get("startRange")).intValue());
			orderNumberSettings.setEndRange(((Number) orderSettings.get("endRange")).intValue());
			orderNumberSettings.setUseSequential((Boolean) orderSettings.get("useSequential"));
			paymentConfig.setOrderNumberSettings(orderNumberSettings);

			// Load pricing settings
			@SuppressWarnings("unchecked")
			Map<String, Object> pricingSettings = (Map<String, Object>) config.get("pricing");
			PricingSettings pricing = new PricingSettings();
			pricing.setMinimumOrderAmount(((Number) pricingSettings.get("minimumOrderAmount")).doubleValue());
			pricing.setDeliveryFee(((Number) pricingSettings.get("deliveryFee")).doubleValue());
			paymentConfig.setPricing(pricing);

			return paymentConfig;

		} catch (IOException e) {
			throw new RuntimeException("Failed to load payment configuration", e);
		}
	}

	public double getTaxRateForProvince(String province) {
		return taxRates.getOrDefault(province, defaultTaxRate);
	}

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
		private String prefix;
		private int startRange;
		private int endRange;
		private boolean useSequential;

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
		private double minimumOrderAmount;
		private double deliveryFee;

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
