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


    /**
     * Test setting and retrieving summary text.
     */
    @Test
    void testSetSummaryText() {

        OrderReceipt receipt = new OrderReceipt();

        receipt.setSummaryText("Order completed successfully");

        assertEquals("Order completed successfully", receipt.getSummaryText());
    }

    /**
     * Test setting and retrieving delivery date.
     */
    @Test
    void testSetDeliveryDate() {

        OrderReceipt receipt = new OrderReceipt();

        LocalDate date = LocalDate.of(2026, 3, 1);
        receipt.setDeliveryDate(date);

        assertEquals(date, receipt.getDeliveryDate());
    }
    /**
     * Test email status flag.
     */
    @Test
    void testEmailSentStatus() {

        OrderReceipt receipt = new OrderReceipt();

        receipt.setEmailSent(true);

        assertTrue(receipt.isEmailSent());
    }
}

