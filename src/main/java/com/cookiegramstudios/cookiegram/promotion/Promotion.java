package com.cookiegramstudios.cookiegram.promotion;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Promotion entity that represents a promotional offer.
 * <p>
 * Running promotions will be displayed on the main landing page that
 * customers are brought to upon successful login; the promotions on that page
 * will be made up of Promotion entities from this class.
 * </p>
 * <p>
 * The entity includes the following key attributes:
 * </p>
 * <ul>
 * <li><b>id: int</b> - Unique identifier for the promotion</li>
 * <li><b>promoCode: String</b> - A unique promotional code</li>
 * <li><b>description: String</b> - Short description about the promotion</li>
 * <li><b>promoType: String</b> - Type of promotion (fixed dollar amount or set percentage)</li>
 * <li><b>promoValue: String</b> - The actual figure (dollar amount or percentage)</li>
 * <li><b>starDate: LocalDateTime</b> - Date and time the promotion started</li>
 * <li><b>endDate: LocalDateTime</b> - Date and time the promotion ended</li>
 * <li><b>isActive: boolean</b> - Whether the promotion is currently running or not</li>
 * </ul>
 *
 * @author Kyle Haines
 * @date 2026-02-22
 * @version 1.0
 */
@Entity
@Table(name = "promotions")
public class Promotion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@Column(nullable = false, unique = true)
	String promoCode;
	@Column(nullable = false)
	String description; // short description
	@Column(nullable = false)
	String promoType; // fixed $ or %
	@Column(nullable = false)
	double promoValue; // fixed $ or %
	@Column(nullable = false)
	LocalDateTime startDate; // YYYY-MM-DD
	@Column(nullable = false)
	LocalDateTime endDate; // YYYY-MM-DD
	@Column(nullable = false)
	boolean isActive; // whether promotion is currently active or not
}
