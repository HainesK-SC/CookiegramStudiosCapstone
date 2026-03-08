package com.cookiegramstudios.cookiegram.receipt;


@DataJpaTest
public class OrderReceiptRepositoryTest {
    // Inject the repository that we want to test
    @Autowired
    private OrderReceiptRepository orderReceiptRepository;

    /**
     * Test that an OrderReceipt can be saved successfully
     * and receives an auto-generated ID.
     */

    @Test
    void testSaveOrderReceipt() {

        // Create a dummy Order object (required by the entity)
        Order order = new Order();

        // Create a new OrderReceipt object
        OrderReceipt receipt = new OrderReceipt();
        receipt.setOrderNumber(1001);
        receipt.setOrder(order);
        receipt.setSummaryText("Test receipt summary");
        receipt.setTotalPrice(25.50);
        receipt.setDeliveryDate(LocalDate.now());
        receipt.setEmailSent(false);

        // Save the receipt to the database
        OrderReceipt savedReceipt = orderReceiptRepository.save(receipt);

        // Verify the receipt was saved and an ID was generated
        assertNotNull(savedReceipt.getId());

        // Verify the stored data is correct
        assertEquals(1001, savedReceipt.getOrderNumber());
        assertEquals(25.50, savedReceipt.getTotalPrice());
    }

    /**
     * Test retrieving an OrderReceipt using the custom
     * repository method findByOrderNumber().
     */
    @Test
    void testFindByOrderNumber() {
        // Create a dummy Order object
        Order order = new Order();

        // Create and save a receipt
        OrderReceipt receipt = new OrderReceipt();
        receipt.setOrderNumber(2002);
        receipt.setOrder(order);
        receipt.setSummaryText("Lookup test receipt");
        receipt.setTotalPrice(40.00);
        receipt.setDeliveryDate(LocalDate.now());

        orderReceiptRepository.save(receipt);

        // Attempt to retrieve the receipt using the custom query method
        OrderReceipt foundReceipt = orderReceiptRepository
                .findByOrderNumber(2002)
                .orElse(null);

        // Verify the receipt was found
        assertNotNull(foundReceipt);
    }
}
