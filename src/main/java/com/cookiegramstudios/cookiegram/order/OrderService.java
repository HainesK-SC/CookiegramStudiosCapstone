package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.cookiegramstudios.cookiegram.user.User;
import org.springframework.stereotype.Service;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.common.config.PaymentConfig;
import com.cookiegramstudios.cookiegram.customer.Customer;
import com.cookiegramstudios.cookiegram.customer.CustomerService;
import com.cookiegramstudios.cookiegram.order.dto.CheckoutFormDTO;
import com.cookiegramstudios.cookiegram.recipient.Recipient;
import com.cookiegramstudios.cookiegram.recipient.RecipientService;

/**
 * Service layer for order-related business operations.
 * <p>
 * Provides application-facing methods for retrieving and managing orders.
 * Acts as an abstraction between controllers and {@link OrderRepository}.
 * Centralizes order creation logic, order number generation, and order notes building.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 2.0
 */
@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final RecipientService recipientService;
    private final PaymentConfig paymentConfig;
 
    public OrderService(OrderRepository orderRepository,
                       CustomerService customerService,
                       RecipientService recipientService,
                       PaymentConfig paymentConfig) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.recipientService = recipientService;
        this.paymentConfig = paymentConfig;
    }
    
    public Order createOrder(OrderCreationRequest request) {
        // 1. Find or create customer
        Customer customer = customerService.findOrCreateCustomer(request.getCheckoutForm());
 
        // 2. Create recipient
        Recipient recipient = recipientService.createRecipientFromCheckoutForm(request.getCheckoutForm());
 
        // 3. Generate unique order number
        int orderNumber = generateUniqueOrderNumber();
 
        // 4. Build order notes
        String notes = buildOrderNotes(request.getCheckoutForm(), request.getCart());
 
        // 5. Create order entity
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setCustomerProfile(customer);
        order.setRecipientUser(recipient);
        order.setStatus(OrderStatus.PLACED);
        order.setDeliveryDate(request.getCheckoutForm().getDeliveryDate());
        order.setTotalPrice(request.getTotalPrice());
        order.setNotes(notes);

        // setApproved boolean
        order.setApproved(false);
        order.setApprovedBy(null);
        order.setApprovedAt(null);
 
        // 6. Save and return
        return orderRepository.save(order);
    }
    
    public int generateUniqueOrderNumber() {
        PaymentConfig.OrderNumberSettings settings = paymentConfig.getOrderNumberSettings();
 
        if (settings.isUseSequential()) {
            // Find the highest existing order number
            Integer lastOrderNumber = orderRepository.findTopByOrderByOrderNumberDesc()
                    .map(Order::getOrderNumber)
                    .orElse(null);
 
            if (lastOrderNumber != null) {
                // Increment from last order
                int nextNumber = lastOrderNumber + 1;
                
                // Ensure we stay within configured range
                if (nextNumber > settings.getEndRange()) {
                    throw new RuntimeException("Order number range exhausted. Maximum: " + settings.getEndRange());
                }
                
                return nextNumber;
            } else {
                // First order - start at beginning of range
                return settings.getStartRange();
            }
        } else {
            // Random order number generation
            return generateRandomOrderNumber(settings.getStartRange(), settings.getEndRange());
        }
    }
    
    private int generateRandomOrderNumber(int startRange, int endRange) {
        int range = endRange - startRange + 1;
        int maxAttempts = 10;
 
        for (int i = 0; i < maxAttempts; i++) {
            int orderNumber = (int) (Math.random() * range) + startRange;
            
            // Check if this number is already used
            if (orderRepository.findByOrderNumber(orderNumber) == null) {
                return orderNumber;
            }
        }
 
        throw new RuntimeException("Unable to generate unique order number after " + maxAttempts + " attempts");
    }
    
    public String buildOrderNotes(CheckoutFormDTO form, Cart cart) {
        StringBuilder notes = new StringBuilder();
 
        // Delivery time preference
        notes.append("Delivery Time Preference: ").append(form.getDeliveryTimePreference()).append("\n");
 
        // Delivery instructions
        if (form.getDeliveryInstructions() != null && !form.getDeliveryInstructions().isEmpty()) {
            notes.append("Delivery Instructions: ").append(form.getDeliveryInstructions()).append("\n");
        }
 
        // Sender phone
        if (form.getSenderPhone() != null && !form.getSenderPhone().isEmpty()) {
            notes.append("Sender Phone: ").append(form.getSenderPhone()).append("\n");
        }
 
        // Custom messages from checkout form
        if (form.getCustomMessages() != null && !form.getCustomMessages().isEmpty()) {
            notes.append("\nCustom Messages:\n");
            for (int i = 0; i < form.getCustomMessages().size(); i++) {
                String message = form.getCustomMessages().get(i);
                if (message != null && !message.trim().isEmpty()) {
                    notes.append("  Item ").append(i + 1).append(": ")
                            .append(message).append("\n");
                }
            }
        }
 
        return notes.toString();
    }


    
    
    
    // Retrieval methods for orders based on various criteria
    
    public List<Order> getTodaysOrders() {
        LocalDate today = LocalDate.now();
        return orderRepository.findByDeliveryDateOrderByCreatedAtAsc(today);
    }


    public List<Order> getOrdersByDeliveryDate(LocalDate date) {
        return orderRepository.findByDeliveryDateOrderByCreatedAtAsc(date);
    }

  
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

 
    public List<Order> getTodaysOrdersByStatus(OrderStatus status) {
        LocalDate today = LocalDate.now();
        return orderRepository.findByDeliveryDateAndStatus(today, status);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public Order findByOrderNumber(int orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    //Approval Methods
    public List<Order> findPending() {
        return orderRepository.findByApprovedFalseOrderByCreatedAtAsc();
    }

    public List<Order> findApprovedOrders() {
        return orderRepository.findByApprovedTrueOrderByApprovedAtDesc();
    }

    public Order approveOrder(Long orderId, User approvedUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setApproved(true);
        order.setApprovedBy(approvedUser);
        order.setApprovedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

}
