package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrace {

    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    private List<StatusLog> logs = new ArrayList<>();

    public void addLog(String status, String changedBy) {
        this.logs.add(new StatusLog(status, Instant.now(), changedBy));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusLog {
        private String status;
        private Instant changedAt;
        private String changedBy;
    }
}
