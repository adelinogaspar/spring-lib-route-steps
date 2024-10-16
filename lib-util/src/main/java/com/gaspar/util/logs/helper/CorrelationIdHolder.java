package com.gaspar.util.logs.helper;

import java.util.UUID;

public class CorrelationIdHolder {
    private static final ThreadLocal<String> correlationId = new ThreadLocal<>();

    public static void setCorrelationId(String id) {
        correlationId.set(id);
    }

    public static String getCorrelationId() {
        return correlationId.get();
    }

    public static void clear() {
        correlationId.remove();
    }

    public static String generateCorrelationId() {
        String uuid = UUID.randomUUID().toString();
        setCorrelationId(uuid);
        return uuid;
    }
}
