package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cookiegramstudios.cookiegram.customer.Customer;
import com.cookiegramstudios.cookiegram.recipient.Recipient;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * 
 */
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
