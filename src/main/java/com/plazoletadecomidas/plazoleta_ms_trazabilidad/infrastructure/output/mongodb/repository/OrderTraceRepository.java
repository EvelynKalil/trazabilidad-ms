package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.repository;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document.OrderTraceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface OrderTraceRepository extends MongoRepository<OrderTraceDocument, UUID> {
    List<OrderTraceDocument> findByCustomerId(UUID customerId);
    List<OrderTraceDocument> findByRestaurantId(UUID restaurantId);
}
