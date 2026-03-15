package com.cookiegramstudios.cookiegram.cart;

import com.cookiegramstudios.cookiegram.product.Product;

public class CartItem {
	private Product productType; // currently can only be a Cookie object
	private int itemQty;
	
	public CartItem() {
		
	}
	
	public CartItem(Product product, int quantity) {
		this.productType = product;
		this.itemQty = quantity;
	}
	
	public Product getProductType() {
		return productType;
	}
	public void setProductType(Product productType) {
		this.productType = productType;
	}
	public int getItemQty() {
		return itemQty;
	}
	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}
	
	
}
