package com.cookiegramstudios.cookiegram.order.dto;

import com.cookiegramstudios.cookiegram.order.PricingService;

/**
 * Data Transfer Object for order pricing calculations.
 * <p>
 * Encapsulates all pricing-related information for an order including
 * subtotal, tax, and total amounts. Provides both raw numeric values
 * and pre-formatted strings for display purposes.
 * </p>
 * <p>
 * This DTO is typically created by {@link PricingService} and consumed
 * by controllers for rendering checkout and confirmation pages.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
public class OrderPricingDTO {

	private double subtotal;
	private double tax;
	private double taxRate;
	private double total;
	private String formattedSubtotal;
	private String formattedTax;
	private String formattedTotal;
	private String province;
	private String currency;

	public OrderPricingDTO() {
	}

	public OrderPricingDTO(double subtotal, double tax, double taxRate, double total, String formattedSubtotal,
			String formattedTax, String formattedTotal, String province, String currency) {
		this.subtotal = subtotal;
		this.tax = tax;
		this.taxRate = taxRate;
		this.total = total;
		this.formattedSubtotal = formattedSubtotal;
		this.formattedTax = formattedTax;
		this.formattedTotal = formattedTotal;
		this.province = province;
		this.currency = currency;
	}
	
	public double getSubtotal() {
        return subtotal;
    }
 
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
 
    public double getTax() {
        return tax;
    }
 
    public void setTax(double tax) {
        this.tax = tax;
    }
 
    public double getTaxRate() {
        return taxRate;
    }
 
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }
 
    public double getTotal() {
        return total;
    }
 
    public void setTotal(double total) {
        this.total = total;
    }
 
    public String getFormattedSubtotal() {
        return formattedSubtotal;
    }
 
    public void setFormattedSubtotal(String formattedSubtotal) {
        this.formattedSubtotal = formattedSubtotal;
    }
 
    public String getFormattedTax() {
        return formattedTax;
    }
 
    public void setFormattedTax(String formattedTax) {
        this.formattedTax = formattedTax;
    }
 
    public String getFormattedTotal() {
        return formattedTotal;
    }
 
    public void setFormattedTotal(String formattedTotal) {
        this.formattedTotal = formattedTotal;
    }
 
    public String getProvince() {
        return province;
    }
 
    public void setProvince(String province) {
        this.province = province;
    }
 
    public String getCurrency() {
        return currency;
    }
 
    public void setCurrency(String currency) {
        this.currency = currency;
    }
 
    @Override
    public String toString() {
        return "OrderPricingDTO{" +
                "subtotal=" + subtotal +
                ", tax=" + tax +
                ", taxRate=" + taxRate +
                ", total=" + total +
                ", province='" + province + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

}
