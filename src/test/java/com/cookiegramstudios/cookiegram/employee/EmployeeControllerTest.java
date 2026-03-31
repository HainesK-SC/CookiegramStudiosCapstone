package com.cookiegramstudios.cookiegram.employee;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import jakarta.servlet.ServletException;

import java.time.LocalDate;
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
import com.cookiegramstudios.cookiegram.recipient.Recipient;
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

    private User buildEmployeeUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("baker@cookiegram.com");
        user.setFirstName("Baker");
        user.setLastName("Smith");
        user.setRole(UserRole.EMPLOYEE);
        user.setPassword("password123");
        return user;
    }

    private Order buildSampleOrder(int orderNumber) {
        Customer customer = new Customer();
        customer.setEmail("customer@example.com");
        customer.setFirstName("Test");
        customer.setLastName("Customer");

        Recipient recipient = new Recipient();
        recipient.setFirstName("Jane");
        recipient.setLastName("Doe");
        recipient.setStreet("123 Maple St");
        recipient.setCity("Burlington");
        recipient.setPostalCode("L7R 1A1");
        recipient.setCountry("Canada");

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setStatus(OrderStatus.PLACED);
        order.setDeliveryDate(LocalDate.now());
        order.setCustomerProfile(customer);
        order.setRecipientUser(recipient);
        return order;
    }

    @Test
    void dashboard_authenticatedEmployee_returnsDashboardView() throws Exception {
        User employee = buildEmployeeUser();
        when(userService.findByEmail("baker@cookiegram.com")).thenReturn(employee);
        when(orderService.getTodaysApprovedOrders()).thenReturn(Collections.emptyList());
        when(orderService.getOtherApprovedOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/employee-dashboard"));
    }

    @Test
    void dashboard_modelContainsCurrentUser() throws Exception {
        User employee = buildEmployeeUser();
        when(userService.findByEmail("baker@cookiegram.com")).thenReturn(employee);
        when(orderService.getTodaysApprovedOrders()).thenReturn(Collections.emptyList());
        when(orderService.getOtherApprovedOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void dashboard_modelContainsTodaysOrders() throws Exception {
        User employee = buildEmployeeUser();
        List<Order> orders = List.of(buildSampleOrder(1001), buildSampleOrder(1002));
        when(userService.findByEmail("baker@cookiegram.com")).thenReturn(employee);
        when(orderService.getTodaysApprovedOrders()).thenReturn(orders);
        when(orderService.getOtherApprovedOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void dashboard_noOrdersToday_modelContainsEmptyList() throws Exception {
        User employee = buildEmployeeUser();
        when(userService.findByEmail("baker@cookiegram.com")).thenReturn(employee);
        when(orderService.getTodaysApprovedOrders()).thenReturn(Collections.emptyList());
        when(orderService.getOtherApprovedOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("orders", Collections.emptyList()));
    }

    @Test
    void updateOrderStatus_validRequest_returnsRedirectToDashboard() throws Exception {
        Order updated = buildSampleOrder(1001);
        updated.setStatus(OrderStatus.IN_PROGRESS);
        when(orderService.updateOrderStatus(1001L, OrderStatus.IN_PROGRESS)).thenReturn(updated);

        mockMvc.perform(post("/employee/orders/1001/status")
                        .with(csrf())
                        .param("status", "IN_PROGRESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/dashboard"));
    }

    @Test
    void updateOrderStatus_bakedStatus_returnsRedirectToDashboard() throws Exception {
        Order updated = buildSampleOrder(1001);
        updated.setStatus(OrderStatus.BAKED);
        when(orderService.updateOrderStatus(1001L, OrderStatus.BAKED)).thenReturn(updated);

        mockMvc.perform(post("/employee/orders/1001/status")
                        .with(csrf())
                        .param("status", "BAKED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/dashboard"));
    }

    @Test
    void updateOrderStatus_orderNotFound_throwsServletException() {
        when(orderService.updateOrderStatus(9999L, OrderStatus.SHIPPED))
                .thenThrow(new RuntimeException("Order not found with ID: 9999"));

        ServletException ex = assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/employee/orders/9999/status")
                        .with(csrf())
                        .param("status", "SHIPPED"))
        );

        assertEquals("Order not found with ID: 9999", ex.getCause().getMessage());
    }

    @Test
    void updateOrderStatus_unexpectedException_throwsServletException() {
        when(orderService.updateOrderStatus(1001L, OrderStatus.DELIVERED))
                .thenThrow(new RuntimeException("Unexpected database error"));

        ServletException ex = assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/employee/orders/1001/status")
                        .with(csrf())
                        .param("status", "DELIVERED"))
        );

        assertEquals("Unexpected database error", ex.getCause().getMessage());
    }
}
