package com.cookiegramstudios.cookiegram.promotion;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Relational entity that represents the relationship between Order and Promotion entities.
 * <p>
 * An order may have 0 or more promotions on it, and a promotion may be on 0 or more Orders.
 * This relational entity handles that relationship, by allowing multiple promotions to be a part
 * of the same order.
 * </p>
 * <p>
 * The entity includes the following key attributes:
 * </p>
 * <ul>
 * <li><b>id: long</b> - Unique identifier for the OrderPromotion object</li>
 * <li><b>promotionId: String</b> - Unique identifier for the promotion</li>
 * <li><b>discountAmt: Double</b> - The total dollar amount of the discount</li>
 * <li><b>appliedAt: LocalDateTime</b> - The date and time at which the promotion was applied (only after checkout)</li>
 * </ul>
 *
 * @author Kyle Haines
 * @date 2026-02-22
 * @version 1.0
 */
@Entity
@Table(name = "order_promotions")
public class OrderPromotion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@Column(nullable = false)

	long orderId; // FK REFERENCE TO ORDER OBJECT
	@Column(nullable = false)

	long promoId; // FK REFERENCE TO PROMOTION OBJECT
	@Column(nullable = false)

	String promoCode; // PROMO CODE USED - HELPFUL IF CODES WILL CHANGE
	@Column(nullable = false)

	double discountAmt;
	@Column(nullable = false)

	LocalDateTime appliedAt;
	
	public OrderPromotion() {
	}
	
	public OrderPromotion(long promoId, String promoCode, double discountAmt, LocalDateTime appliedAt) {
		this.promoId = promoId;
		this.promoCode = promoCode;
		this.discountAmt = discountAmt;
		this.appliedAt = appliedAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getPromoId() {
		return promoId;
	}

	public void setPromoId(long promoId) {
		this.promoId = promoId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public double getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(double discountAmt) {
		this.discountAmt = discountAmt;
	}

	public LocalDateTime getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(LocalDateTime appliedAt) {
		this.appliedAt = appliedAt;
	}
	
	
}
