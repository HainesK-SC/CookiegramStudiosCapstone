package com.cookiegramstudios.cookiegram.promotion;

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

public class OrderPromotion {
	long id;
	long orderId; // FK REFERENCE TO ORDER OBJECT
	long promoId; // FK REFERENCE TO PROMOTION OBJECT
	String promoCode; // PROMO CODE USED - HELPFUL IF CODES WILL CHANGE
	double discountAmt;
	LocalDateTime appliedAt;	
}
