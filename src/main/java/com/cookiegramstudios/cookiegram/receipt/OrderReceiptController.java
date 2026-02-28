package com.cookiegramstudios.cookiegram.receipt;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


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
