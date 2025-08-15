package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;

import java.util.List;
import java.util.UUID;

public interface OrderTraceServicePort {
    void registerStatusChange(UUID orderId, UUID customerId, UUID restaurantId, String status, String changedBy);
    OrderTrace getOrderTrace(UUID orderId, UUID customerId);
    List<OrderTrace> getOrdersByRestaurant(UUID restaurantId);
    void save(OrderTrace trace, String token);
}
