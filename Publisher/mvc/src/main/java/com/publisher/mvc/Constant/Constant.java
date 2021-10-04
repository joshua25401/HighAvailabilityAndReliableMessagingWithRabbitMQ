package com.publisher.mvc.Constant;

public class Constant {
    // Define Queue Name
    public static final String[] QUEUE_NAMES = { "payment_queue", "topup_queue" };

    // Define Routing Keys
    public static final String[] ROUTING_KEYS = { "payments", "top_up" };

    // Define Exchange
    public static final String EXCHANGE = "BANK_DEL";
}
