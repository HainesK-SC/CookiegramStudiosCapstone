package com.cookiegramstudios.cookiegram.receipt;

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
}
