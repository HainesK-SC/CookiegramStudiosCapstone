package com.cookiegramstudios.cookiegram.order;

import org.springframework.stereotype.Component;

import com.cookiegramstudios.cookiegram.common.config.PaymentConfig;

/**
 * Resolves effective province and tax rate for pricing calculations.
 * 
 * @author Matthew Samaha
 * @date 2026-03-18
 */
@Component
public class TaxRateResolver {
	
	private final PaymentConfig paymentConfig;

    public TaxRateResolver(PaymentConfig paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    public String resolveProvince(String province) {
        if (province == null || province.trim().isEmpty()) {
            return paymentConfig.getDefaultProvince();
        }
        return province.toUpperCase();
    }

    public double resolveTaxRate(String province) {
        String effectiveProvince = resolveProvince(province);
        return paymentConfig.getTaxRateForProvince(effectiveProvince);
    }

}
