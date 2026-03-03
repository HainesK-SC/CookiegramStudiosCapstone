package com.cookiegramstudios.cookiegram.receipt;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for transferring OrderReceipt data.
 *
 * @name Nguyen Anh Khoa Tran
 * @date 2026-02-28
 * @version 1.0
 */
@Data
public class OrderReceiptDTO {
    private int orderNumber;
    private String summaryText;
    private double totalPrice;
    private LocalDate deliveryDate;
    private LocalDateTime createdAt;
    private boolean emailSent;
}
