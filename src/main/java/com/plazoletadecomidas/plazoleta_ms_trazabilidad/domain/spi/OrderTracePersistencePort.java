package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;

import java.util.List;
import java.util.UUID;

public interface OrderTracePersistencePort {
    void save(OrderTrace trace);
    OrderTrace findById(UUID orderId);
    List<OrderTrace> findByCustomerId(UUID customerId);
    List<OrderTrace> findByRestaurantId(UUID restaurantId);
}
