package com.cookiegramstudios.cookiegram.utils;

import com.cookiegramstudios.cookiegram.cart.Cart;
import com.cookiegramstudios.cookiegram.order.Order;

import jakarta.servlet.http.HttpSession;

/**
 * Utility class for managing HTTP session attributes.
 * <p>
 * Provides type-safe access to session objects and centralizes
 * session attribute name constants. This helps prevent the use
 * of magic strings and reduces casting errors across the codebase.
 * </p>
 *
 * <p><b>Provided functionality includes:</b></p>
 * <ul>
 *   <li><b>Cart Management:</b>
 *     <ul>
 *       <li>{@code getCart(HttpSession)} – Retrieves the Cart object from the session.</li>
 *       <li>{@code setCart(HttpSession, Cart)} – Stores the Cart object in the session.</li>
 *       <li>{@code clearCart(HttpSession)} – Removes the Cart object from the session.</li>
 *       <li>{@code getOrCreateCart(HttpSession)} – Retrieves the Cart or creates a new one if it does not exist.</li>
 *     </ul>
 *   </li>
 *   <li><b>Order Management:</b>
 *     <ul>
 *       <li>{@code getConfirmedOrder(HttpSession)} – Retrieves the confirmed Order from the session.</li>
 *       <li>{@code setConfirmedOrder(HttpSession, Order)} – Stores the confirmed Order in the session.</li>
 *       <li>{@code clearConfirmedOrder(HttpSession)} – Removes the confirmed Order from the session.</li>
 *     </ul>
 *   </li>
 *   <li><b>Session Cleanup:</b>
 *     <ul>
 *       <li>{@code clearOrderSession(HttpSession)} – Clears all order-related session data, including cart, confirmed order, and checkout data.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
public class SessionHelper {
	
	private static final String CART_ATTRIBUTE = "cart";
    private static final String CONFIRMED_ORDER_ATTRIBUTE = "confirmedOrder";
    private static final String CHECKOUT_DATA_ATTRIBUTE = "checkoutData";

   
    private SessionHelper() {
        throw new UnsupportedOperationException("Utility class - do not instantiate");
    }
 
    
    public static Cart getCart(HttpSession session) {
        return (Cart) session.getAttribute(CART_ATTRIBUTE);
    }
 
    
    public static void setCart(HttpSession session, Cart cart) {
        session.setAttribute(CART_ATTRIBUTE, cart);
    }
 
   
    public static void clearCart(HttpSession session) {
        session.removeAttribute(CART_ATTRIBUTE);
    }
 
   
    public static Order getConfirmedOrder(HttpSession session) {
        return (Order) session.getAttribute(CONFIRMED_ORDER_ATTRIBUTE);
    }
 
   
    public static void setConfirmedOrder(HttpSession session, Order order) {
        session.setAttribute(CONFIRMED_ORDER_ATTRIBUTE, order);
    }
 
   
    public static void clearConfirmedOrder(HttpSession session) {
        session.removeAttribute(CONFIRMED_ORDER_ATTRIBUTE);
    }
 
   
    public static void clearOrderSession(HttpSession session) {
        session.removeAttribute(CART_ATTRIBUTE);
        session.removeAttribute(CONFIRMED_ORDER_ATTRIBUTE);
        session.removeAttribute(CHECKOUT_DATA_ATTRIBUTE);
    }
 
    
    public static Cart getOrCreateCart(HttpSession session) {
        Cart cart = getCart(session);
        if (cart == null) {
            cart = new Cart();
            setCart(session, cart);
        }
        return cart;
    }

}
