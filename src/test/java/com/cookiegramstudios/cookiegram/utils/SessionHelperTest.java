package com.cookiegramstudios.cookiegram.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.order.Order;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class SessionHelperTest {

	@Mock
	private HttpSession session;

	@Test
	void getCart_returnsCartFromSession() {
		Cart cart = mock(Cart.class);
		when(session.getAttribute("cart")).thenReturn(cart);

		Cart result = SessionHelper.getCart(session);

		assertSame(cart, result);
		verify(session).getAttribute("cart");
	}

	@Test
	void setCart_setsCartAttribute() {
		Cart cart = mock(Cart.class);

		SessionHelper.setCart(session, cart);

		verify(session).setAttribute("cart", cart);
	}

	@Test
	void clearCart_removesCartAttribute() {
		SessionHelper.clearCart(session);

		verify(session).removeAttribute("cart");
	}

	@Test
	void getConfirmedOrder_returnsOrderFromSession() {
		Order order = mock(Order.class);
		when(session.getAttribute("confirmedOrder")).thenReturn(order);

		Order result = SessionHelper.getConfirmedOrder(session);

		assertSame(order, result);
		verify(session).getAttribute("confirmedOrder");
	}

	@Test
	void setConfirmedOrder_setsOrderAttribute() {
		Order order = mock(Order.class);

		SessionHelper.setConfirmedOrder(session, order);

		verify(session).setAttribute("confirmedOrder", order);
	}

	@Test
	void clearConfirmedOrder_removesOrderAttribute() {
		SessionHelper.clearConfirmedOrder(session);

		verify(session).removeAttribute("confirmedOrder");
	}

	@Test
	void clearOrderSession_removesAllOrderRelatedAttributes() {
		SessionHelper.clearOrderSession(session);

		verify(session).removeAttribute("cart");
		verify(session).removeAttribute("confirmedOrder");
		verify(session).removeAttribute("checkoutData");
	}

	@Test
	void getOrCreateCart_whenCartExists_returnsExistingWithoutSettingNewCart() {
		Cart existingCart = mock(Cart.class);
		when(session.getAttribute("cart")).thenReturn(existingCart);

		Cart result = SessionHelper.getOrCreateCart(session);

		assertSame(existingCart, result);
		verify(session).getAttribute("cart");
		verify(session, never()).setAttribute(eq("cart"), any(Cart.class));
	}

	@Test
	void getOrCreateCart_whenCartMissing_createsAndStoresNewCart() {
		when(session.getAttribute("cart")).thenReturn(null);

		Cart result = SessionHelper.getOrCreateCart(session);

		assertNotNull(result);
		verify(session).getAttribute("cart");
		verify(session).setAttribute("cart", result);
	}

	@Test
	void privateConstructor_throwsUnsupportedOperationException() throws Exception {
		Constructor<SessionHelper> constructor = SessionHelper.class.getDeclaredConstructor();
		constructor.setAccessible(true);

		InvocationTargetException ex = assertThrows(InvocationTargetException.class, constructor::newInstance);

		assertTrue(ex.getCause() instanceof UnsupportedOperationException);
		assertEquals("Utility class - do not instantiate", ex.getCause().getMessage());
	}
}