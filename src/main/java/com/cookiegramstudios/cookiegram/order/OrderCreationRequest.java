package com.cookiegramstudios.cookiegram.order;

import com.cookiegramstudios.cookiegram.cart.Cart;

/**
 * Data Transfer Object for order creation requests.
 * <p>
 * Encapsulates all the information needed to create a new order,
 * including the shopping cart, checkout form data, and calculated pricing.
 * Used to transfer data from OrderController to OrderService.
 * </p>
 * <p>
 * This DTO simplifies the order creation process by bundling related
 * data into a single object rather than passing multiple parameters.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
public class OrderCreationRequest {
	
	private Cart cart;
	
	private CheckoutFormDTO checkoutForm;
	
	private double totalPrice;
	
	public OrderCreationRequest() {
    }
	
	public OrderCreationRequest(Cart cart, CheckoutFormDTO checkoutForm, double totalPrice) {
        this.cart = cart;
        this.checkoutForm = checkoutForm;
        this.totalPrice = totalPrice;
    }
	
	public Cart getCart() {
        return cart;
    }
 
    public void setCart(Cart cart) {
        this.cart = cart;
    }
 
    public CheckoutFormDTO getCheckoutForm() {
        return checkoutForm;
    }
 
    public void setCheckoutForm(CheckoutFormDTO checkoutForm) {
        this.checkoutForm = checkoutForm;
    }
 
    public double getTotalPrice() {
        return totalPrice;
    }
 
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
 
    @Override
    public String toString() {
        return "OrderCreationRequest{" +
                "cart=" + cart +
                ", checkoutForm=" + checkoutForm +
                ", totalPrice=" + totalPrice +
                '}';
    }


}
