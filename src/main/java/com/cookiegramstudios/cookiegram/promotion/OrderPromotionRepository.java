package com.cookiegramstudios.cookiegram.promotion;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link OrderPromotion} relational entity.
 * <p>
 * Provides CRUD operations and custom queries. Extends {@link JpaRepository}
 * </p>
 *
 * @author Kyle Haines
 * @date 2026-02-22
 * @version 1.0
 */
@Repository
public interface OrderPromotionRepository extends JpaRepository<OrderPromotion, Long> {

	OrderPromotion findByPromoCode(String promoCode);

	OrderPromotion findByOrderId(long orderId);

	OrderPromotion findByPromoId(long promoId);

	OrderPromotion findByDiscountAmt(double discountAmt);

	OrderPromotion findByAppliedAt(LocalDateTime appliedAt);
}
