package com.cookiegramstudios.cookiegram.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link OrderService}.
 * <p>
 * Verifies that each service method correctly delegates to
 * {@link OrderRepository} with the right arguments, and handles edge cases like
 * missing orders. Plain unit tests — no Spring context required.
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
		sampleOrder.setOrderNumber(1001);
		sampleOrder.setStatus(OrderStatus.PLACED); // Default status for new orders
		sampleOrder.setDeliveryDate(LocalDate.now());
	}

	/**
	 * getTodaysOrders
	 */
	@Test
	void getTodaysOrders_delegatesToRepositoryWithTodaysDate() {
		LocalDate today = LocalDate.now();
		when(orderRepository.findByDeliveryDateOrderByCreatedAtAsc(today)).thenReturn(List.of(sampleOrder));

		List<Order> result = orderService.getTodaysOrders();

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(orderRepository).findByDeliveryDateOrderByCreatedAtAsc(today);
	}

	/**
	 * getTodaysOrders should return an empty list when no orders exist for today.
	 */
	@Test
	void getTodaysOrders_noOrders_returnsEmptyList() {
		LocalDate today = LocalDate.now();
		when(orderRepository.findByDeliveryDateOrderByCreatedAtAsc(today)).thenReturn(Collections.emptyList());

		List<Order> result = orderService.getTodaysOrders();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * getOrdersByDeliveryDate
	 */
	@Test
	void getOrdersByDeliveryDate_delegatesToRepositoryWithGivenDate() {
		LocalDate date = LocalDate.of(2026, 4, 15);
		when(orderRepository.findByDeliveryDateOrderByCreatedAtAsc(date)).thenReturn(List.of(sampleOrder));

		List<Order> result = orderService.getOrdersByDeliveryDate(date);

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(orderRepository).findByDeliveryDateOrderByCreatedAtAsc(date);
	}

	/**
	 * getOrdersByDeliveryDate should return an empty list when no orders match.
	 */
	@Test
	void getOrdersByDeliveryDate_noMatch_returnsEmptyList() {
		LocalDate date = LocalDate.of(2026, 12, 31);
		when(orderRepository.findByDeliveryDateOrderByCreatedAtAsc(date)).thenReturn(Collections.emptyList());

		List<Order> result = orderService.getOrdersByDeliveryDate(date);

		assertEquals(0, result.size());
	}

	/**
	 * getOrdersByStatus
	 */
	@Test
	void getOrdersByStatus_delegatesToRepositoryWithGivenStatus() {
		when(orderRepository.findByStatus(OrderStatus.PLACED)).thenReturn(List.of(sampleOrder));

		List<Order> result = orderService.getOrdersByStatus(OrderStatus.PLACED);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(OrderStatus.PLACED, result.get(0).getStatus());
		verify(orderRepository).findByStatus(OrderStatus.PLACED);
	}

	/**
	 * getOrdersByStatus should work for every status value in the enum.
	 */
	@Test
	void getOrdersByStatus_inProgressStatus_delegatesCorrectly() {
		Order inProgressOrder = new Order();
		inProgressOrder.setStatus(OrderStatus.IN_PROGRESS);

		when(orderRepository.findByStatus(OrderStatus.IN_PROGRESS)).thenReturn(List.of(inProgressOrder));

		List<Order> result = orderService.getOrdersByStatus(OrderStatus.IN_PROGRESS);

		assertEquals(1, result.size());
		assertEquals(OrderStatus.IN_PROGRESS, result.get(0).getStatus());
	}

	@Test
	void getOrdersByStatus_noMatch_returnsEmptyList() {
		when(orderRepository.findByStatus(OrderStatus.DELIVERED)).thenReturn(Collections.emptyList());

		List<Order> result = orderService.getOrdersByStatus(OrderStatus.DELIVERED);

		assertEquals(0, result.size());
	}

	/**
	 * getTodaysOrdersByStatus
	 */
	@Test
	void getTodaysOrdersByStatus_delegatesWithTodayAndStatus() {
		LocalDate today = LocalDate.now();
		when(orderRepository.findByDeliveryDateAndStatus(today, OrderStatus.PLACED)).thenReturn(List.of(sampleOrder));

		List<Order> result = orderService.getTodaysOrdersByStatus(OrderStatus.PLACED);

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(orderRepository).findByDeliveryDateAndStatus(today, OrderStatus.PLACED);
	}

	/**
	 * getTodaysOrdersByStatus should return empty list when no orders match.
	 */
	@Test
	void getTodaysOrdersByStatus_noMatch_returnsEmptyList() {
		LocalDate today = LocalDate.now();
		when(orderRepository.findByDeliveryDateAndStatus(today, OrderStatus.SHIPPED))
				.thenReturn(Collections.emptyList());

		List<Order> result = orderService.getTodaysOrdersByStatus(OrderStatus.SHIPPED);

		assertEquals(0, result.size());
	}

	/**
	 * updateOrderStatus
	 */
	@Test
	void updateOrderStatus_validId_updatesAndSavesOrder() {
		when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
		when(orderRepository.save(sampleOrder)).thenReturn(sampleOrder);

		Order result = orderService.updateOrderStatus(1L, OrderStatus.IN_PROGRESS);

		assertNotNull(result);
		assertEquals(OrderStatus.IN_PROGRESS, result.getStatus());
		verify(orderRepository).findById(1L);
		verify(orderRepository).save(sampleOrder);
	}

	/**
	 * updateOrderStatus should progress through each status correctly.
	 */
	@Test
	void updateOrderStatus_placedToBaked_updatesCorrectly() {
		sampleOrder.setStatus(OrderStatus.PLACED);
		when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
		when(orderRepository.save(sampleOrder)).thenReturn(sampleOrder);

		Order result = orderService.updateOrderStatus(1L, OrderStatus.BAKED);

		assertEquals(OrderStatus.BAKED, result.getStatus());
	}

	/**
	 * updateOrderStatus with an unknown ID should throw a RuntimeException and
	 * never call save.
	 */
	@Test
	void updateOrderStatus_unknownId_throwsRuntimeException() {
		when(orderRepository.findById(999L)).thenReturn(Optional.empty());

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> orderService.updateOrderStatus(999L, OrderStatus.SHIPPED));

		assertEquals("Order not found with ID: 999", ex.getMessage());
		verify(orderRepository, never()).save(any());
	}

	/**
	 * findByOrderNumber
	 */
	@Test
	void findByOrderNumber_existingNumber_returnsOrder() {
		when(orderRepository.findByOrderNumber(1001)).thenReturn(sampleOrder);

		Order result = orderService.findByOrderNumber(1001);

		assertNotNull(result);
		assertEquals(1001, result.getOrderNumber());
		verify(orderRepository).findByOrderNumber(1001);
	}

	/**
	 * findByOrderNumber should return null when the order number does not exist,
	 * since the repository returns null for unmatched custom query methods.
	 */
	@Test
	void findByOrderNumber_unknownNumber_returnsNull() {
		when(orderRepository.findByOrderNumber(9999)).thenReturn(null);

		Order result = orderService.findByOrderNumber(9999);

		assertEquals(null, result);
		verify(orderRepository).findByOrderNumber(9999);
	}

}
