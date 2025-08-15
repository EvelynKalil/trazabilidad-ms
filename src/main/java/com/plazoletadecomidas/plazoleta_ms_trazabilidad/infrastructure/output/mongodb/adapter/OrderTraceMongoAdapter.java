package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.adapter;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.OrderTracePersistencePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.mapper.OrderTraceMapper;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.repository.OrderTraceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderTraceMongoAdapter implements OrderTracePersistencePort {

    private final OrderTraceRepository repository;
    private final OrderTraceMapper mapper;

    @Override
    public void save(OrderTrace trace) {
        repository.save(mapper.toDocument(trace));
    }

    @Override
    public OrderTrace findById(UUID orderId) {
        return repository.findById(orderId)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<OrderTrace> findByCustomerId(UUID customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTrace> findByRestaurantId(UUID restaurantId) {
        return repository.findByRestaurantId(restaurantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
