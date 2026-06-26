package com.ecommerce.common.kafka;

public final class KafkaTopics {

    public static final String ORDER_CREATED      = "order-created";
    public static final String STOCK_RESERVED     = "stock-reserved";
    public static final String PAYMENT_COMPLETED  = "payment-completed";
    public static final String DEAD_LETTER_QUEUE  = "dead-letter-queue";

    private KafkaTopics() {
    }
}
