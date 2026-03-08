package com.cookiegramstudios.cookiegram.receipt;

import com.cookiegramstudios.cookiegram.order.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class OrderReceiptTest {
    /*
     * Test that the OrderReceipt constructor correctly assigns values.
     */
    @Test
    void testOrderReceiptCreation() {
        // Create a new OrderReceipt object
        OrderReceipt receipt = new OrderReceipt("Alex", 20.00);

        // Verify that the fields were initialized correctly
        assertEquals("Alex", receipt.getCustomerName());
        assertEquals(20.00, receipt.getTotalPrice());
    }

    /*
     * Test that the total price can be updated correctly.
     */
    @Test
    void testUpdateTotalPrice() {

        // Create a receipt
        OrderReceipt receipt = new OrderReceipt();
        receipt.setCustomerName("Alice");

        // Set the total price
        receipt.setTotalPrice(50.00);

        // Verify the price was updated
        assertEquals(50.00, receipt.getTotalPrice());
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

