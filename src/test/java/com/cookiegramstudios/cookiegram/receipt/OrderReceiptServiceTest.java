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
}
