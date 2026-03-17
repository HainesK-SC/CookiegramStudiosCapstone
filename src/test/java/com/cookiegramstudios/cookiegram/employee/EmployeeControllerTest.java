package com.cookiegramstudios.cookiegram.employee;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cookiegramstudios.cookiegram.auth.EmployeeController;
import com.cookiegramstudios.cookiegram.customer.Customer;
import com.cookiegramstudios.cookiegram.order.Order;
import com.cookiegramstudios.cookiegram.order.OrderService;
import com.cookiegramstudios.cookiegram.order.OrderStatus;
import com.cookiegramstudios.cookiegram.user.User;
import com.cookiegramstudios.cookiegram.user.UserRole;
import com.cookiegramstudios.cookiegram.user.UserService;

/**
 * Unit tests for {@link EmployeeController}.
 * <p>
 * Covers GET /employee/dashboard and POST /employee/order/update-status,
 * including authentication, model population, and status update responses.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-17
 * @version 1.0
 */
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = true)
@WithMockUser(username = "baker@cookiegram.com", roles = "EMPLOYEE")
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private OrderService orderService;

	/**
	 * Builds a mock User with the BAKER role for use as the authenticated
	 * principal.
	 */
	private User buildEmployeeUser() {
		User user = new User();
		user.setId(1L);
		user.setEmail("baker@cookiegram.com");
		user.setFirstName("Baker");
		user.setLastName("Smith");
		user.setRole(UserRole.EMPLOYEE); // maps for baker/employee access
		user.setPassword("password123");
		return user;
	}

	/**
	 * Builds a sample Order in PLACED status for today's delivery.
	 */
	private Order buildSampleOrder(int orderNumber) {
	    Customer customer = new Customer();
	    customer.setEmail("customer@example.com");

	    Order order = new Order();
	    order.setId((long) orderNumber);
	    order.setOrderNumber(orderNumber);
	    order.setStatus(OrderStatus.PLACED);
	    order.setDeliveryDate(LocalDate.now());
	    order.setCreatedAt(LocalDateTime.now());
	    order.setCustomerProfile(customer);
	    return order;
	}

	/**
	 * GET /employee/dashboard
	 */

	/**
	 * An authenticated employee should see the dashboard view.
	 */
	@Test
	void dashboard_authenticatedEmployee_returnsDashboardView() throws Exception {
		User employee = buildEmployeeUser();
		when(userService.findByEmail("baker@cookiegram.com")).thenReturn(employee);
		when(orderService.getTodaysOrders()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/employee/dashboard")).andExpect(status().isOk())
				.andExpect(view().name("employee/employee-dashboard"));
	}

	/**
	 * The dashboard model should contain the authenticated user.
	 */
	@Test
	void dashboard_modelContainsCurrentUser() throws Exception {
		User employee = buildEmployeeUser();
		when(userService.findByEmail("baker@cookiegram.com")).thenReturn(employee);
		when(orderService.getTodaysOrders()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/employee/dashboard")).andExpect(status().isOk())
				.andExpect(model().attributeExists("user"));
	}

	/**
	 * The dashboard model should contain today's orders list.
	 */
	@Test
	void dashboard_modelContainsTodaysOrders() throws Exception {
		User employee = buildEmployeeUser();
		List<Order> orders = List.of(buildSampleOrder(1001), buildSampleOrder(1002));
		when(userService.findByEmail("baker@cookiegram.com")).thenReturn(employee);
		when(orderService.getTodaysOrders()).thenReturn(orders);

		mockMvc.perform(get("/employee/dashboard")).andExpect(status().isOk())
				.andExpect(model().attributeExists("orders"));
	}

	/**
	 * The dashboard should render correctly when there are no orders today.
	 */
	@Test
	void dashboard_noOrdersToday_modelContainsEmptyList() throws Exception {
		User employee = buildEmployeeUser();
		when(userService.findByEmail("baker@cookiegram.com")).thenReturn(employee);
		when(orderService.getTodaysOrders()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/employee/dashboard")).andExpect(status().isOk())
				.andExpect(model().attribute("orders", Collections.emptyList()));
	}

	/**
	 * POST /employee/order/update-status
	 */
	/**
	 * A valid orderId and status should return 200 OK with a success message.
	 */
	@Test
	void updateOrderStatus_validRequest_returns200WithSuccessMessage() throws Exception {
		Order updated = buildSampleOrder(1001);
		updated.setStatus(OrderStatus.IN_PROGRESS);
		when(orderService.updateOrderStatus(1001L, OrderStatus.IN_PROGRESS)).thenReturn(updated);

		mockMvc.perform(post("/employee/order/update-status").with(csrf()).param("orderId", "1001").param("newStatus",
				"IN_PROGRESS")).andExpect(status().isOk()).andExpect(content().string("Status updated successfully"));
	}

	/**
	 * Each valid OrderStatus value should be accepted and return 200 OK.
	 */
	@Test
	void updateOrderStatus_bakedStatus_returns200() throws Exception {
		Order updated = buildSampleOrder(1001);
		updated.setStatus(OrderStatus.BAKED);
		when(orderService.updateOrderStatus(1001L, OrderStatus.BAKED)).thenReturn(updated);

		mockMvc.perform(
				post("/employee/order/update-status").with(csrf()).param("orderId", "1001").param("newStatus", "BAKED"))
				.andExpect(status().isOk()).andExpect(content().string("Status updated successfully"));
	}

	/**
	 * When the order is not found, the service throws a RuntimeException and the
	 * controller should return 400 BAD_REQUEST with an error message.
	 */
	@Test
	void updateOrderStatus_orderNotFound_returns400WithErrorMessage() throws Exception {
		when(orderService.updateOrderStatus(9999L, OrderStatus.SHIPPED))
				.thenThrow(new RuntimeException("Order not found with ID: 9999"));

		mockMvc.perform(post("/employee/order/update-status").with(csrf()).param("orderId", "9999").param("newStatus",
				"SHIPPED")).andExpect(status().isBadRequest())
				.andExpect(content().string("Failed to update status: Order not found with ID: 9999"));
	}

	/**
	 * Any unexpected service exception should return 400 BAD_REQUEST with the
	 * exception message in the body.
	 */
	@Test
	void updateOrderStatus_unexpectedException_returns400() throws Exception {
		when(orderService.updateOrderStatus(1001L, OrderStatus.DELIVERED))
				.thenThrow(new RuntimeException("Unexpected database error"));

		mockMvc.perform(post("/employee/order/update-status").with(csrf()).param("orderId", "1001").param("newStatus",
				"DELIVERED")).andExpect(status().isBadRequest())
				.andExpect(content().string("Failed to update status: Unexpected database error"));
	}

}
