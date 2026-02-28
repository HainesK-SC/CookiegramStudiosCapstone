package com.cookiegramstudios.cookiegram.receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderReceiptService {
    private static final Logger logger = LoggerFactory.getLogger(OrderReceiptService.class);
    private final OrderReceiptRepository receiptRepository;

    public OrderReceiptService(OrderReceiptRepository receiptRepo) {
        this.receiptRepository = receiptRepo;
    }

    /**
     * Generates a new receipt for a completed order.
     */
    @Transactional
    public OrderReceipt generateReceipt(Order order) {
        logger.info("Generating receipt for Order ID: {}", order.getId());

        OrderReceipt receipt = new OrderReceipt();
        receipt.setOrder(order);
        receipt.setOrderNumber(order.hashCode()); // Example logic for unique number
        receipt.setTotalPrice(order.getTotalAmount());
        receipt.setDeliveryDate(LocalDate.now().plusDays(3)); // Default logic

        // Auto-generate summary from Order details
        receipt.setSummaryText("Receipt for " + order.getCustomerName() + ". Total: $" + order.getTotalAmount());

        return receiptRepository.save(receipt);
    }

    @Transactional(readOnly = true)
    public OrderReceipt getByOrderNumber(int orderNumber) {
        return receiptRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Receipt not found for number: " + orderNumber));
    }

}

