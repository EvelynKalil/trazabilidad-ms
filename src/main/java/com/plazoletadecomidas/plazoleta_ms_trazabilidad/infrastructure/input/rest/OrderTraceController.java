package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.input.rest;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.OrderEfficiencyDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler.OrderTraceHandler;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trazabilidad")
@RequiredArgsConstructor
public class OrderTraceController {

    private final OrderTraceHandler handler;

    // HU 17 - consultar trazabilidad
    @GetMapping("/orders/{orderId}")
    public OrderTrace getOrderTrace(
            @PathVariable UUID orderId,
            @RequestHeader("Authorization") String token
    ) {
        return handler.getOrderTrace(orderId, token);
    }

    // HU 18 - eficiencia por pedido
    @GetMapping("/efficiency/orders")
    public List<OrderEfficiencyDto> efficiencyOrders(
            @RequestParam UUID restaurantId
    ) {
        return handler.efficiencyOrders(restaurantId);
    }
}
