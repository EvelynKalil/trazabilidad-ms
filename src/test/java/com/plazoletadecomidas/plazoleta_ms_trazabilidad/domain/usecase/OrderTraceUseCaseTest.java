package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.OrderTracePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderTraceUseCaseTest {

    private OrderTracePersistencePort persistencePort;
    private OrderTraceUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = mock(OrderTracePersistencePort.class);
        useCase = new OrderTraceUseCase(persistencePort);
    }

    @Test
    void registerStatusChange_createsNewTraceIfNotExists() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        when(persistencePort.findById(orderId)).thenReturn(null);

        useCase.registerStatusChange(orderId, customerId, restaurantId, "PENDIENTE", "system");

        ArgumentCaptor<OrderTrace> captor = ArgumentCaptor.forClass(OrderTrace.class);
        verify(persistencePort).save(captor.capture());

        OrderTrace saved = captor.getValue();
        assertEquals(orderId, saved.getOrderId());
        assertEquals(1, saved.getLogs().size());
        assertEquals("PENDIENTE", saved.getLogs().get(0).getStatus());
    }

    @Test
    void getOrderTrace_returnsTraceIfOwnerMatches() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        OrderTrace trace = new OrderTrace(orderId, customerId, UUID.randomUUID(), List.of());

        when(persistencePort.findById(orderId)).thenReturn(trace);

        OrderTrace result = useCase.getOrderTrace(orderId, customerId);

        assertEquals(trace, result);
    }

    @Test
    void getOrderTrace_throwsIfNotOwner() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        OrderTrace trace = new OrderTrace(orderId, UUID.randomUUID(), UUID.randomUUID(), List.of());

        when(persistencePort.findById(orderId)).thenReturn(trace);

        assertThrows(RuntimeException.class, () -> useCase.getOrderTrace(orderId, customerId));
    }

    @Test
    void getOrdersByRestaurant_returnsListFromPort() {
        UUID restaurantId = UUID.randomUUID();
        List<OrderTrace> expected = List.of(new OrderTrace());

        when(persistencePort.findByRestaurantId(restaurantId)).thenReturn(expected);

        List<OrderTrace> result = useCase.getOrdersByRestaurant(restaurantId);

        assertEquals(expected, result);
    }

    @Test
    void save_mergesLogsIfExists() {
        UUID orderId = UUID.randomUUID();
        OrderTrace existing = new OrderTrace(orderId, UUID.randomUUID(), UUID.randomUUID(), List.of());
        OrderTrace newTrace = new OrderTrace(orderId, UUID.randomUUID(), UUID.randomUUID(), List.of(new OrderTrace.StatusLog("ENTREGADO", null, "user")));

        when(persistencePort.findByOrderId(orderId)).thenReturn(Optional.of(existing));

        useCase.save(newTrace, "token");

        verify(persistencePort).save(existing);
        assertEquals(1, existing.getLogs().size());
    }

    @Test
    void save_savesDirectlyIfNotExists() {
        UUID orderId = UUID.randomUUID();
        OrderTrace trace = new OrderTrace(orderId, UUID.randomUUID(), UUID.randomUUID(), List.of());

        when(persistencePort.findByOrderId(orderId)).thenReturn(Optional.empty());

        useCase.save(trace, "token");

        verify(persistencePort).save(trace);
    }
}
