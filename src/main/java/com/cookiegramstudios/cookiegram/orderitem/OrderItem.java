package com.cookiegramstudios.cookiegram.orderitem;

import com.cookiegramstudios.cookiegram.customization.CookieCustomization;
import com.cookiegramstudios.cookiegram.order.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cookie_customization_id", nullable = false)
	private CookieCustomization cookieCustomization;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = false)
	private double price;
	
	@Column(columnDefinition = "TEXT")
	private String specialInstructions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public CookieCustomization getCookieCustomization() {
		return cookieCustomization;
	}

	public void setCookieCustomization(CookieCustomization cookieCustomization) {
		this.cookieCustomization = cookieCustomization;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", order=" + order + ", cookieCustomization=" + cookieCustomization
				+ ", quantity=" + quantity + ", price=" + price + ", specialInstructions=" + specialInstructions + "]";
	}
}
