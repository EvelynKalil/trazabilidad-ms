package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders_trazabilidad")
public class OrderTraceDocument {

    @Id
    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;

    @Builder.Default
    private List<StatusLog> logs = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusLog {
        private String status;
        private Instant changedAt;
        private String changedBy;
    }
}
