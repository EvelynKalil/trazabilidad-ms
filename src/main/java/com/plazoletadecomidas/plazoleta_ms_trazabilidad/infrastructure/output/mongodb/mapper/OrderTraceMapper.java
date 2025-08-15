package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.mapper;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document.OrderTraceDocument;
import org.springframework.stereotype.Component;


@Component
public class OrderTraceMapper {

    public OrderTrace toDomain(OrderTraceDocument doc) {
        return new OrderTrace(
                doc.getOrderId(),
                doc.getCustomerId(),
                doc.getRestaurantId(),
                doc.getLogs().stream()
                        .map(log -> new OrderTrace.StatusLog(log.getStatus(), log.getChangedAt(), log.getChangedBy()))
                        .toList()
        );
    }

    public OrderTraceDocument toDocument(OrderTrace domain) {
        return OrderTraceDocument.builder()
                .orderId(domain.getOrderId())
                .customerId(domain.getCustomerId())
                .restaurantId(domain.getRestaurantId())
                .logs(domain.getLogs().stream()
                        .map(log -> OrderTraceDocument.StatusLog.builder()
                                .status(log.getStatus())
                                .changedAt(log.getChangedAt())
                                .changedBy(log.getChangedBy())
                                .build())
                        .toList())
                .build();
    }
}
