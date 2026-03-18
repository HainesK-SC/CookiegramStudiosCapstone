package com.cookiegramstudios.cookiegram.order;

import com.cookiegramstudios.cookiegram.common.config.PaymentConfig;

public class PricingService {
	
	private final PaymentConfig paymentConfig;
	
	public PricingService(PaymentConfig paymentConfig) {
		this.paymentConfig = paymentConfig;
	}

}
