package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api.OrderTraceServicePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.OrderTracePersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderTraceUseCase implements OrderTraceServicePort {

    private final OrderTracePersistencePort persistencePort;

    @Override
    public void registerStatusChange(UUID orderId, UUID customerId, UUID restaurantId, String status, String changedBy) {
        OrderTrace trace = persistencePort.findById(orderId);
        if (trace == null) {
            trace = new OrderTrace(orderId, customerId, restaurantId, List.of());
        }
        trace.addLog(status, changedBy);
        persistencePort.save(trace);
    }

    @Override
    public OrderTrace getOrderTrace(UUID orderId, UUID customerId) {
        OrderTrace trace = persistencePort.findById(orderId);
        if (trace == null || !trace.getCustomerId().equals(customerId)) {
            throw new RuntimeException("No autorizado o no encontrado");
        }
        return trace;
    }

    @Override
    public List<OrderTrace> getOrdersByRestaurant(UUID restaurantId) {
        return persistencePort.findByRestaurantId(restaurantId);
    }

    @Override
    public void save(OrderTrace trace, String token) {
        // Aquí podrías validar el token si lo necesitas para seguridad
        persistencePort.save(trace);
    }
}
