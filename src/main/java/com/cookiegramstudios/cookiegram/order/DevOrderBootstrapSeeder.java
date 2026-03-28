package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.cookiegramstudios.cookiegram.customer.Customer;
import com.cookiegramstudios.cookiegram.customer.CustomerRepository;
import com.cookiegramstudios.cookiegram.recipient.Recipient;
import com.cookiegramstudios.cookiegram.recipient.RecipientRepository;
import com.cookiegramstudios.cookiegram.user.User;
import com.cookiegramstudios.cookiegram.user.UserRepository;

@Component
@Profile("dev")
public class DevOrderBootstrapSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevOrderBootstrapSeeder.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RecipientRepository recipientRepository;
    private final UserRepository userRepository;

    public DevOrderBootstrapSeeder(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            RecipientRepository recipientRepository,
            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.recipientRepository = recipientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        logger.info("Bootstrapping order seed data...");

        List<Customer> customers = customerRepository.findAll();
        List<Recipient> recipients = recipientRepository.findAll();
        List<User> users = userRepository.findAll();

        if (customers.isEmpty() || recipients.isEmpty() || users.isEmpty()) {
            logger.warn("Skipping order seed data — customers, recipients, or users not yet seeded.");
            return;
        }

        Customer c1 = customers.get(0);
        Customer c2 = customers.size() > 1 ? customers.get(1) : customers.get(0);

        Recipient r1 = recipients.get(0);
        Recipient r2 = recipients.size() > 1 ? recipients.get(1) : recipients.get(0);

        User approver = users.get(0);

        // Order 1 — approved, delivered
        createOrderIfMissing(
                1001, c1, r1, OrderStatus.DELIVERED,
                LocalDate.of(2026, 3, 10), true, approver,
                LocalDateTime.of(2026, 3, 9, 10, 0),
                "Leave at front door.",
                List.of(
                        new OrderItem(null, 1L, "Classic Chocolate Chip Cookie", 3.50, 6),
                        new OrderItem(null, 2L, "Snickerdoodle Cookie", 3.00, 4)
                )
        );

        // Order 2 — approved, in progress
        createOrderIfMissing(
                1002, c2, r2, OrderStatus.IN_PROGRESS,
                LocalDate.of(2026, 4, 5), true, approver,
                LocalDateTime.of(2026, 4, 3, 14, 30),
                null,
                List.of(
                        new OrderItem(null, 3L, "Red Velvet Cookie", 4.00, 12)
                )
        );

        // Order 3 — not yet approved, placed
        createOrderIfMissing(
                1003, c1, r2, OrderStatus.PLACED,
                LocalDate.of(2026, 4, 20), false, null,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                "Please include a birthday card.",
                List.of(
                        new OrderItem(null, 1L, "Classic Chocolate Chip Cookie", 3.50, 3),
                        new OrderItem(null, 4L, "Lemon Glazed Cookie", 3.75, 3)
                )
        );
        
        // Order 4 - approved, placed, today's date
        createOrderIfMissing(
                1004, c1, r2, OrderStatus.PLACED,
                LocalDate.now(), true, null,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                "Nothing",
                List.of(
                        new OrderItem(null, 1L, "Classic Chocolate Chip Cookie", 3.50, 3),
                        new OrderItem(null, 4L, "Lemon Glazed Cookie", 3.75, 3)
                )
        );
    }

    private void createOrderIfMissing(
            int orderNumber,
            Customer customer,
            Recipient recipient,
            OrderStatus status,
            LocalDate deliveryDate,
            boolean approved,
            User approvedBy,
            LocalDateTime createdAt,
            String notes,
            List<OrderItem> items) {

        if (orderRepository.findByOrderNumber(orderNumber) != null) {
            logger.info("Order already exists. Skipping bootstrap: #{}", orderNumber);
            return;
        }

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setCustomerProfile(customer);
        order.setRecipientUser(recipient);
        order.setStatus(status);
        order.setDeliveryDate(deliveryDate);
        order.setApproved(approved);
        order.setApprovedBy(approvedBy);
        order.setApprovedAt(approved ? createdAt.plusHours(1) : null);
        order.setCreatedAt(createdAt);
        order.setNotes(notes);

        for (OrderItem item : items) {
            order.addItem(item);
        }

        double total = items.stream().mapToDouble(OrderItem::getLineTotal).sum();
        order.setTotalPrice(total);

        orderRepository.save(order);
        logger.info("Bootstrapped order: #{} — {} item(s), total ${}", orderNumber, items.size(), total);
    }
}