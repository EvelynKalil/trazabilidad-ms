package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderTrace {

    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    private List<StatusLog> logs = new ArrayList<>();

    public OrderTrace() {}

    public OrderTrace(UUID orderId, UUID customerId, UUID restaurantId, List<StatusLog> logs) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.logs = logs;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public List<StatusLog> getLogs() {
        return logs;
    }

    public void addLog(String status, String changedBy) {
        this.logs.add(new StatusLog(status, Instant.now(), changedBy));
    }

    public static class StatusLog {
        private String status;
        private Instant changedAt;
        private String changedBy;

        public StatusLog() {}

        public StatusLog(String status, Instant changedAt, String changedBy) {
            this.status = status;
            this.changedAt = changedAt;
            this.changedBy = changedBy;
        }

        public String getStatus() {
            return status;
        }

        public Instant getChangedAt() {
            return changedAt;
        }

        public String getChangedBy() {
            return changedBy;
        }
    }
}
