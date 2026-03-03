package com.cookiegramstudios.cookiegram.receipt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller for handling OrderReceipt-related endpoints.
 *
 * @name Nguyen Anh Khoa Tran
 * @date 2026-02-28
 * @version 1.0
 */
@Controller
public class OrderReceiptController {
    private final OrderReceiptService receiptService;

    public OrderReceiptController(OrderReceiptService service) {
        this.receiptService = service;
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderReceipt> getReceipt(@PathVariable int orderNumber) {
        return ResponseEntity.ok(receiptService.getByOrderNumber(orderNumber));
    }
}
