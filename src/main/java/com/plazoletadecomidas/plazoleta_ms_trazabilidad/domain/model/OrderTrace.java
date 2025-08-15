package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderTrace {

    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    private List<StatusLog> logs = new ArrayList<>();

    public OrderTrace(UUID orderId, UUID customerId, UUID restaurantId, List<StatusLog> logs) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.logs = new ArrayList<>(logs != null ? logs : List.of());
    }

    public void addLog(String status, String changedBy) {
        this.logs.add(new StatusLog(status, Instant.now(), changedBy));
    }

    @Data
    @NoArgsConstructor
    public static class StatusLog {
        private String status;
        private Instant changedAt;
        private String changedBy;

        public StatusLog(String status, Instant changedAt, String changedBy) {
            this.status = status;
            this.changedAt = changedAt;
            this.changedBy = changedBy;
        }
    }
}
