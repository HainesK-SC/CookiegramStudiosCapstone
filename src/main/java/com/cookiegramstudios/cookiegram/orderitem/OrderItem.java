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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents an individual item within a production order in the CookieGram system.
 * <p>
 * Each OrderItem links to a parent Order and a specific CookieCustomization,
 * tracking quantity, price, and optional special instructions.
 * </p>
 * <p>
 * <b>Fields include:</b>
 * <ul>
 *     <li><b>id</b> - Unique identifier for the order item</li>
 *     <li><b>order</b> - Parent order (Many-to-One)</li>
 *     <li><b>cookieCustomization</b> - The cookie variant or customization</li>
 *     <li><b>quantity</b> - Number of this item in the order</li>
 *     <li><b>price</b> - Total price for this item (base + customization cost × quantity)</li>
 *     <li><b>specialInstructions</b> - Optional instructions for this item</li>
 * </ul>
 * </p>
 * 
 * <p><b>Future Considerations:</b>
 * <ul>
 *     <li>Support ingredient-level customization tracking</li>
 *     <li>Integration with inventory management</li>
 *     <li>Automated price calculation based on recipe and customization</li>
 * </ul>
 * </p>
 * 
 * @author Matthew
 * @date 2026-02-28
 * @version 1.0
 */
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
	
	 /**
     * Calculates and updates the price based on quantity and cookie customization costs.
     * <p>
     * Formula: price = quantity × (basePrice + customizationCost)
     * </p>
     */
	// --- Lifecycle Hooks ---
    @PrePersist
    @PreUpdate
    private void calculatePrice() {
        if (cookieCustomization != null && cookieCustomization.getCookie() != null) {
            this.price = quantity * (cookieCustomization.getCookie().getBasePrice() + cookieCustomization.getAdditionalCost());
        } else {
            this.price = 0;
        }
    }

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
