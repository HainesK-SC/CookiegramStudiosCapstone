package com.cookiegramstudios.cookiegram.receipt;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrderReceiptDTO {
    private int orderNumber;
    private String summaryText;
    private double totalPrice;
    private LocalDate deliveryDate;
    private LocalDateTime createdAt;
    private boolean emailSent;
}
