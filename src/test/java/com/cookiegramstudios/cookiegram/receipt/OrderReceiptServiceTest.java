package com.cookiegramstudios.cookiegram.receipt;

import com.cookiegramstudios.cookiegram.order.Order;
import com.cookiegramstudios.cookiegram.receipt.OrderReceipt;
import com.cookiegramstudios.cookiegram.receipt.OrderReceiptRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class OrderReceiptServiceTest {
    @Mock
    private OrderReceiptRepository receiptRepository;

    @InjectMocks
    private OrderReceiptService receiptService;

    @Test
    void testGenerateReceipt_DataMapping() {
        // ARRANGE: Create a dummy Order object to be "processed"
        Order order = new Order();
        order.setId(500L);
        order.setCustomerName("Alex Smith");
        order.setTotalAmount(45.99);

        // Tell Mockito to return whatever object it is asked to save (simulating a DB save)
        when(receiptRepository.save(any(OrderReceipt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT: Generate the receipt
        OrderReceipt receipt = receiptService.generateReceipt(order);

        // ASSERT: Check if the receipt was populated with the correct Order data
        assertNotNull(receipt, "The generated receipt should not be null");
        assertEquals(45.99, receipt.getTotalPrice(), "Price must match the Order total");

        // Verify the summaryText logic we wrote actually contains the customer's name
        assertTrue(receipt.getSummaryText().contains("Alex Smith"), "Summary should include customer name");

        // Confirm the receipt is linked to the correct Order ID
        assertEquals(500L, receipt.getOrder().getId());
    }
}
