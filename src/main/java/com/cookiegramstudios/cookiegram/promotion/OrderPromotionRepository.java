package com.cookiegramstudios.cookiegram.promotion;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link OrderPromotion} relational entity.
 * <p>
 *     Provides CRUD operations and custom queries.
 *     Extends {@link JpaRepository}
 * </p>
 *
 * @author Kyle Haines
 * @date 2026-02-22
 * @version 1.0
 */
@Repository
public interface OrderPromotionRepository extends JpaRepository<OrderPromotion, Long> {
	/**
     * Find an OrderPromotion(s) by promotion code
     * @param the unique promotion code
     * @return List: OrderPromotion - A list of OrderPromotion object(s) of the given promotion code
     */
	OrderPromotion findByPromoCode(String promoCode);
	
	/**
     * Find an OrderPromotion(s) by orderId
     * @param the unique order ID
     * @return List: OrderPromotion - A list of OrderPromotion object(s) of the given order ID
     */
	OrderPromotion findByOrderId(long orderId);
	
	/**
     * Find an OrderPromotion(s) by promoId
     * @param the unique promo ID
     * @return List: OrderPromotion - A list of OrderPromotion object(s) of the given promo ID
     */
	OrderPromotion findByPromoId(long promoId);
	
	/**
     * Find a OrderPromotion(s) by the amount discounted
     * @param the dollar value of discount applied to the order
     * @return List: OrderPromotion - A list of OrderPromotion object(s) of the given discount amount
     */
	OrderPromotion findByDiscountAmt(double discountAmt);
	
	/**
     * Find OrderPromotion(s) by the date and time the promotion was applied
     * @param the date and time the order was placed and promotion was applied
     * @return List: OrderPromotion - List of OrderPromotion object(s) that were applied at the same time
     */
	OrderPromotion findByAppliedAt(LocalDateTime appliedAt);
}
