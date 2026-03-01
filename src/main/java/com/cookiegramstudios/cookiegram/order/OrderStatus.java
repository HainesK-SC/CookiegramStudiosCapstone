package com.cookiegramstudios.cookiegram.order;

/**
 * Enum representing the different states of an Order in the CookieGram system.
 * <p>
 * Orders progress through a defined workflow from placement to delivery.
 * </p>
 * <ul>
 *     <li><b>PLACED</b> - Order has been created but processing has not started</li>
 *     <li><b>IN_PROGRESS</b> - Order is actively being prepared or baked</li>
 *     <li><b>BAKED</b> - Order preparation is complete</li>
 *     <li><b>SHIPPED</b> - Order has left the bakery for delivery</li>
 *     <li><b>DELIVERED</b> - Order has been successfully delivered to the recipient</li>
 * </ul>
 * 
 * @author Matthew
 * @date 2026-02-28
 * @version 1.0
 */
public enum OrderStatus {
	
	PLACED,
	IN_PROGRESS,
	BAKED,
	SHIPPED,
	DELIVERED,

}
