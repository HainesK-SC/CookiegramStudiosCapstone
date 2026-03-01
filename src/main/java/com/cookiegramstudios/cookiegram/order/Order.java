package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cookiegramstudios.cookiegram.customer.Customer;
import com.cookiegramstudios.cookiegram.recipient.Recipient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Represents a production order within the CookieGram system.
 * <p>
 * Orders are internal entities that track customer purchases,
 * recipient details, and order workflow statuses. This entity
 * is used for managing order processing, delivery scheduling,
 * and administrative notes.
 * </p>
 * <p>
 * <b>Fields include:</b>
 * <ul>
 *     <li><b>id</b> - Unique identifier for the order</li>
 *     <li><b>orderNumber</b> - System-generated unique number for tracking</li>
 *     <li><b>customerProfile</b> - The customer who placed the order</li>
 *     <li><b>recipientUser</b> - Recipient of the order</li>
 *     <li><b>status</b> - Current workflow status (PLACED, IN_PROGRESS, BAKED, SHIPPED, DELIVERED)</li>
 *     <li><b>deliveryDate</b> - Scheduled delivery date</li>
 *     <li><b>totalPrice</b> - Total amount of the order</li>
 *     <li><b>notes</b> - Optional administrative or special instructions</li>
 *     <li><b>createdAt</b> - Timestamp when the order was created</li>
 *     <li><b>updatedAt</b> - Timestamp when the order was last updated</li>
 * </ul>
 * </p>
 * <p>
 * <b>Future Considerations:</b>
 * <ul>
 *     <li>Integrate with receipts and payment records</li>
 *     <li>Support automated status updates for delivery tracking</li>
 *     <li>Enhance auditing and reporting features</li>
 * </ul>
 * </p>
 * 
 * @author Matthew Samaha
 * @date 2026-02-28
 * @version 1.0
 */
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private int orderNumber;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customerProfile;

	@ManyToOne
	@JoinColumn(name = "recipient_id", nullable = false)
	private Recipient recipientUser;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	private LocalDate deliveryDate;

	private double totalPrice;

	@Column(columnDefinition = "TEXT")
	private String notes;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedat;

	// Lifecycle Hooks
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedat = LocalDateTime.now();
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Customer getCustomerProfile() {
		return customerProfile;
	}

	public void setCustomerProfile(Customer customerProfile) {
		this.customerProfile = customerProfile;
	}

	public Recipient getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUser(Recipient recipientUser) {
		this.recipientUser = recipientUser;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedat() {
		return updatedat;
	}

	public void setUpdatedat(LocalDateTime updatedat) {
		this.updatedat = updatedat;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderNumber=" + orderNumber + ", customerProfile=" + customerProfile
				+ ", recipientUser=" + recipientUser + ", status=" + status + ", deliveryDate=" + deliveryDate
				+ ", totalPrice=" + totalPrice + ", notes=" + notes + ", createdAt=" + createdAt + ", updatedat="
				+ updatedat + "]";
	}

}
