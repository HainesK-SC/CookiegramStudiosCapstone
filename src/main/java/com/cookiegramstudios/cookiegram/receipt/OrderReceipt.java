package com.cookiegramstudios.cookiegram.receipt;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cookiegramstudios.cookiegram.order.Order;

/**
 * OrderReceipt entity representing a generated receipt for a completed order.
 *
 * @name Nguyen Anh Khoa Tran
 * @date 2026-02-28
 * @version 1.0	
 */
@Entity
@Table(name = "order_receipts")
@NoArgsConstructor
@AllArgsConstructor
public class OrderReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private int orderNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Edit: Matthew - Assuming an Order entity exists :: must implement

    @Column(columnDefinition = "TEXT")
    private String summaryText;

    private double totalPrice;

    private LocalDate deliveryDate;

    private LocalDateTime createdAt;

    private boolean emailSent;

    private LocalDateTime emailSentAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Edit: Matthew - Additional methods :: @Getters and @Setters causes issuess and compilation errors, so we will add them manually

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

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getSummaryText() {
		return summaryText;
	}

	public void setSummaryText(String summaryText) {
		this.summaryText = summaryText;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isEmailSent() {
		return emailSent;
	}

	public void setEmailSent(boolean emailSent) {
		this.emailSent = emailSent;
	}

	public LocalDateTime getEmailSentAt() {
		return emailSentAt;
	}

	public void setEmailSentAt(LocalDateTime emailSentAt) {
		this.emailSentAt = emailSentAt;
	}

	@Override
	public String toString() {
		return "OrderReceipt [id=" + id + ", orderNumber=" + orderNumber + ", summaryText=" + summaryText
				+ ", totalPrice=" + totalPrice + ", deliveryDate=" + deliveryDate + ", createdAt=" + createdAt
				+ ", emailSent=" + emailSent + ", emailSentAt=" + emailSentAt + "]";
	}
 
}
