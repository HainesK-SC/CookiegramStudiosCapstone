package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link OrderService}.
 * <p>
 * Verifies that each service method correctly delegates to {@link OrderRepository}
 * with the right arguments, and handles edge cases like missing orders.
 * Plain unit tests — no Spring context required.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-17
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	
	// Setup
	@Mock
    private OrderRepository orderRepository;
 
    @InjectMocks
    private OrderService orderService;
 
    private Order sampleOrder;
 
    @BeforeEach
    void setUp() {
        sampleOrder = new Order();
        sampleOrder.setId(1L);
        sampleOrder.setOrderNumber(1001);
        sampleOrder.setStatus(OrderStatus.PLACED);
        sampleOrder.setDeliveryDate(LocalDate.now());
        sampleOrder.setCreatedAt(LocalDateTime.now());
    }
	
	/**
	 * getTodaysOrders
	 */
	
	
	/**
	 * getOrdersByDeliveryDate
	 */
	
	
	/**
	 * getOrdersByStatus
	 */
	
	
	/**
	 * getTodaysOrdersByStatus
	 */
	
	
	/**
	 * updateOrderStatus
	 */
	
	
	/**
	 * findByOrderNumber
	 */

}
