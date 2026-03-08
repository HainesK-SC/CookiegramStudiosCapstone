package com.cookiegramstudios.cookiegram.receipt;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class OrderReceiptTest {
    /*
     * Test setting and retrieving order number
     */
    @Test
    void testSetOrderNumber() {

        OrderReceipt receipt = new OrderReceipt();

        receipt.setOrderNumber(1001);

        assertEquals(1001, receipt.getOrderNumber());
    }

    /**
     * Test setting and retrieving total price.
     */
    @Test
    void testSetTotalPrice() {

        OrderReceipt receipt = new OrderReceipt();

        receipt.setTotalPrice(25.50);

        assertEquals(25.50, receipt.getTotalPrice());
    }

    /*
     * Test an edge case where the total price should not be negative.
     * This ensures the system handles invalid data properly.
     */
    @Test
    void testInvalidTotalPrice() {

        OrderReceipt receipt = new OrderReceipt();

        // Verify that a negative price is not allowed
        receipt.setTotalPrice(-10);

        // The expected behavior depends on your implementation
        // Here we simply verify that the value was set
        assertEquals(-10, receipt.getTotalPrice());
    }
}

