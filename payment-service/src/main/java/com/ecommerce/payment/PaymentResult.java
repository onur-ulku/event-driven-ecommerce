package com.ecommerce.payment;

import lombok.Getter;

/*
 * Ödeme sonucunu taşıyan sınıf.
 * success → ödeme başarılı mı?
 * failureReason → başarısızsa neden? Başarılıysa null.
 */
@Getter
public class PaymentResult {

    private final boolean success;
    private final String failure;

    private PaymentResult(boolean success, String failure) {
        this.success = success;
        this.failure = failure;
    }

    public static PaymentResult success() {
        return new PaymentResult(true, null);
    }

    public static PaymentResult failure(String reason) {
        return new PaymentResult(false, reason);
    }
}
