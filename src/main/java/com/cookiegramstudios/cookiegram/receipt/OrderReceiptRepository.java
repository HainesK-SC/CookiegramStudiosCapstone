package com.cookiegramstudios.cookiegram.receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderReceiptRepository extends JpaRepository<OrderReceipt, Long> {
    Optional<OrderReceipt> findByOrderNumber(int orderNumber);
}
