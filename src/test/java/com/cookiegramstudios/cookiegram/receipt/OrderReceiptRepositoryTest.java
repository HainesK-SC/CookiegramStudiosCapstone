package com.cookiegramstudios.cookiegram.receipt;

@DataJpaTest
public class OrderReceiptRepositoryTest {
    @Autowired
    private OrderReceiptRepository receiptRepository;

    @Autowired
    private TestEntityManager entityManager; // Used to set up related entities like Order

    @Test
    void testFindByOrderNumber() {
        // ARRANGE: Create and persist a parent Order first (due to OneToOne requirement)
        Order order = new Order();
        order.setCustomerName("Test User");
        order.setTotalAmount(10.00);
        entityManager.persist(order); // Saves the order so it has an ID

}
