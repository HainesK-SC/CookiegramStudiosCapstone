package com.cookiegramstudios.cookiegram.promotion;

/**
 * Simple enumeration to keep order type options between fixed and percentage.
 * <p>
 * Promotions can be of two types: fixed or percentage. Fixed promotions will
 * discount a set dollar amount. While a percentage based promotion will take a
 * percentage off of the order total.
 * </p>
 * <p>
 * The enum includes the following options:
 * </p>
 * <ul>
 * <li><b>FIXED</b> - Discount by fixed dollar amount</li>
 * <li><b>PERCENTAGE</b> - Discount by a percentage of the total</li>
 * </ul>
 *
 * @author Kyle Haines
 * @date 2026-02-22
 * @version 1.0
 */

public enum PromotionTypes {
	FIXED, // by dollar amount
	PERCENTAGE // by percentage amount
}
