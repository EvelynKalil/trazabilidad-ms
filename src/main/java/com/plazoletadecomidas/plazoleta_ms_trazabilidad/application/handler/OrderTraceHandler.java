package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.OrderEfficiencyDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.OrderTraceRequestDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api.OrderTraceServicePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.security.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderTraceHandler {

    private final OrderTraceServicePort service;
    private final AuthValidator authValidator;
    private final OrderTraceServicePort servicePort;

    // HU 17
    public OrderTrace getOrderTrace(UUID orderId, String token) {
        UUID customerId = authValidator.validate(token, Role.CLIENTE);
        return service.getOrderTrace(orderId, customerId);
    }

    // HU 18
    public List<OrderEfficiencyDto> efficiencyOrders(UUID restaurantId) {
        return service.getOrdersByRestaurant(restaurantId).stream()
                .map(order -> {
                    var start = order.getLogs().stream()
                            .filter(l -> "PENDIENTE".equalsIgnoreCase(l.getStatus()))
                            .findFirst();
                    var end = order.getLogs().stream()
                            .filter(l -> "ENTREGADO".equalsIgnoreCase(l.getStatus()))
                            .findFirst();
                    if (start.isPresent() && end.isPresent()) {
                        long minutes = java.time.Duration.between(start.get().getChangedAt(), end.get().getChangedAt()).toMinutes();
                        return new OrderEfficiencyDto(order.getOrderId(), minutes);
                    }
                    return null;
                })
                .filter(o -> o != null)
                .toList();
    }

    public void saveOrderTrace(OrderTraceRequestDto dto, String token) {

        UUID userId = authValidator.validate(token, null);
        String role = authValidator.getRoleFromToken(token);

        OrderTrace trace = new OrderTrace();
        trace.setOrderId(dto.getOrderId());
        trace.setCustomerId(dto.getCustomerId());
        trace.setRestaurantId(dto.getRestaurantId());

        // Guardar el log con rol e ID
        trace.addLog(dto.getStatus(), role + " - " + userId);

        servicePort.save(trace, token);
    }



}
