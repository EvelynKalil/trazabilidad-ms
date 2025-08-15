package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.mapper;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.OrderEfficiencyDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OrderTraceDtoMapper {

    public OrderEfficiencyDto toEfficiencyDto(OrderTrace orderTrace) {
        var start = orderTrace.getLogs().stream()
                .filter(l -> "PENDIENTE".equalsIgnoreCase(l.getStatus()))
                .findFirst();
        var end = orderTrace.getLogs().stream()
                .filter(l -> "ENTREGADO".equalsIgnoreCase(l.getStatus()))
                .findFirst();

        if (start.isPresent() && end.isPresent()) {
            long minutes = Duration.between(start.get().getChangedAt(), end.get().getChangedAt()).toMinutes();
            return new OrderEfficiencyDto(orderTrace.getOrderId(), minutes);
        }
        return null; // o lanzar excepción si quieres que siempre tenga inicio y fin
    }
}
