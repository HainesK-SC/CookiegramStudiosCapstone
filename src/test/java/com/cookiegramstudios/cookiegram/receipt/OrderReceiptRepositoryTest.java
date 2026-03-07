package com.cookiegramstudios.cookiegram.receipt;

@DataJpaTest
public class OrderReceiptRepositoryTest {
    @Autowired
    private OrderReceiptRepository receiptRepository;

    @Autowired
    private TestEntityManager entityManager; // Used to set up related entities like Order

}
