package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.EmployeeEfficiencyDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.OrderEfficiencyDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.OrderTraceRequestDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api.OrderTraceServicePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.security.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderTraceHandlerTest {

    private OrderTraceServicePort service;
    private OrderTraceServicePort servicePort;
    private AuthValidator authValidator;
    private OrderTraceHandler handler;

    @BeforeEach
    void setUp() {
        service = mock(OrderTraceServicePort.class);
        servicePort = mock(OrderTraceServicePort.class);
        authValidator = mock(AuthValidator.class);
        handler = new OrderTraceHandler(service, authValidator, servicePort);
    }

    @Test
    void getOrderTrace_shouldReturnTraceWhenAuthorized() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        OrderTrace trace = new OrderTrace();
        when(authValidator.validate("Bearer token", Role.CLIENTE)).thenReturn(customerId);
        when(service.getOrderTrace(orderId, customerId)).thenReturn(trace);

        OrderTrace result = handler.getOrderTrace(orderId, "Bearer token");

        assertThat(result).isSameAs(trace);
        verify(authValidator).validate("Bearer token", Role.CLIENTE);
        verify(service).getOrderTrace(orderId, customerId);
    }

    @Test
    void efficiencyOrders_shouldReturnMinutesBetweenPendingAndDelivered() {
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        when(authValidator.validate(anyString(), eq(Role.PROPIETARIO))).thenReturn(ownerId);

        OrderTrace trace = new OrderTrace();
        trace.setOrderId(UUID.randomUUID());
        trace.addLog("PENDIENTE", "system");
        trace.getLogs().get(0).setChangedAt(Instant.parse("2025-08-15T10:00:00Z"));
        trace.addLog("ENTREGADO", "system");
        trace.getLogs().get(1).setChangedAt(Instant.parse("2025-08-15T10:30:00Z"));

        when(service.getOrdersByRestaurant(restaurantId)).thenReturn(List.of(trace));

        List<OrderEfficiencyDto> result = handler.efficiencyOrders(restaurantId, "Bearer token");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMinutes()).isEqualTo(30);
    }

    @Test
    void efficiencyByEmployee_shouldGroupByEmployeeAndAverageTime() {
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        when(authValidator.validate(anyString(), eq(Role.PROPIETARIO))).thenReturn(ownerId);

        OrderTrace trace = new OrderTrace();
        trace.addLog("PENDIENTE", "system");
        trace.getLogs().get(0).setChangedAt(Instant.parse("2025-08-15T10:00:00Z"));
        trace.addLog("ENTREGADO", "EMPLOYEE-1");
        trace.getLogs().get(1).setChangedAt(Instant.parse("2025-08-15T10:20:00Z"));

        when(service.getOrdersByRestaurant(restaurantId)).thenReturn(List.of(trace));

        List<EmployeeEfficiencyDto> result = handler.efficiencyByEmployee(restaurantId, "Bearer token");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmployeeId()).isEqualTo("EMPLOYEE-1");
        assertThat(result.get(0).getAvgMinutes()).isEqualTo(20);
    }

    @Test
    void saveOrderTrace_shouldCallServicePortSaveWithBuiltTrace() {
        UUID userId = UUID.randomUUID();
        when(authValidator.validate(anyString(), isNull())).thenReturn(userId);
        when(authValidator.getRoleFromToken(anyString())).thenReturn("CLIENTE");

        OrderTraceRequestDto dto = new OrderTraceRequestDto();
        dto.setOrderId(UUID.randomUUID());
        dto.setCustomerId(UUID.randomUUID());
        dto.setRestaurantId(UUID.randomUUID());
        dto.setStatus("PENDIENTE");

        handler.saveOrderTrace(dto, "Bearer token");

        ArgumentCaptor<OrderTrace> captor = ArgumentCaptor.forClass(OrderTrace.class);
        verify(servicePort).save(captor.capture(), eq("Bearer token"));

        OrderTrace saved = captor.getValue();
        assertThat(saved.getOrderId()).isEqualTo(dto.getOrderId());
        assertThat(saved.getLogs()).hasSize(1);
        assertThat(saved.getLogs().get(0).getStatus()).isEqualTo("PENDIENTE");
        assertThat(saved.getLogs().get(0).getChangedBy()).contains("CLIENTE - " + userId);
    }
}
