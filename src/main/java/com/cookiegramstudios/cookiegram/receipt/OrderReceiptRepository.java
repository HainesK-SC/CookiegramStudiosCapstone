package com.cookiegramstudios.cookiegram.receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for OrderReceipt persistence operations.
 *
 * @name Nguyen Anh Khoa Tran
 * @date 2026-02-28
 * @version 1.0
 */
@Repository
public interface OrderReceiptRepository extends JpaRepository<OrderReceipt, Long> {
    Optional<OrderReceipt> findByOrderNumber(int orderNumber);
}
