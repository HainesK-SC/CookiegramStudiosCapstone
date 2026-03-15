package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Service layer for order-related business operations.
 * <p>
 * Provides application-facing methods for retrieving and managing orders.
 * Acts as an abstraction between controllers and {@link OrderRepository}.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-14
 * @version 1.0
 */
@Service
public class OrderService {
	
	private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


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

}
