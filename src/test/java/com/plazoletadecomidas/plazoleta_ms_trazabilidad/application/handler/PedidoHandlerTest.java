package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.ItemDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoRequestDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoResponseDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.mapper.PedidoRequestMapper;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api.PedidoServicePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PedidoHandlerTest {

    private PedidoServicePort pedidoServicePort;
    private PedidoRequestMapper pedidoRequestMapper;
    private PedidoHandler pedidoHandler;

    @BeforeEach
    void setUp() {
        pedidoServicePort = mock(PedidoServicePort.class);
        pedidoRequestMapper = mock(PedidoRequestMapper.class);
        pedidoHandler = new PedidoHandler(pedidoServicePort, pedidoRequestMapper);
    }

    @Test
    @DisplayName("Debe crear pedido correctamente")
    void crearPedido_ok() {
        UUID clienteId = UUID.randomUUID();
        String token = "Bearer token";
        PedidoRequestDto dto = new PedidoRequestDto(
                clienteId,
                List.of(new ItemDto(UUID.randomUUID(), 2))
        );

        Pedido pedidoMock = new Pedido();
        pedidoMock.setClienteId(clienteId);

        PedidoResponseDto responseDto = new PedidoResponseDto(
                UUID.randomUUID(),
                clienteId,
                dto.restauranteId(),
                dto.items(),
                "PENDIENTE",
                LocalDateTime.now()
        );

        when(pedidoRequestMapper.toPedido(dto, clienteId)).thenReturn(pedidoMock);
        when(pedidoServicePort.crear(any(Pedido.class), eq(token))).thenReturn(pedidoMock);
        when(pedidoRequestMapper.toResponseDto(pedidoMock)).thenReturn(responseDto);

        PedidoResponseDto result = pedidoHandler.crearPedido(dto, clienteId, token);

        assertNotNull(result);
        assertEquals("PENDIENTE", result.estado());
        verify(pedidoRequestMapper).toPedido(dto, clienteId);
        verify(pedidoServicePort).crear(pedidoMock, token);
        verify(pedidoRequestMapper).toResponseDto(pedidoMock);
    }

    @Test
    @DisplayName("Debe propagar excepción del service")
    void crearPedido_error() {
        UUID clienteId = UUID.randomUUID();
        String token = "Bearer token";
        PedidoRequestDto dto = new PedidoRequestDto(
                clienteId,
                List.of(new ItemDto(UUID.randomUUID(), 2))
        );

        Pedido pedidoMock = new Pedido();

        when(pedidoRequestMapper.toPedido(dto, clienteId)).thenReturn(pedidoMock);
        when(pedidoServicePort.crear(any(Pedido.class), eq(token)))
                .thenThrow(new IllegalArgumentException("Error de negocio"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoHandler.crearPedido(dto, clienteId, token)
        );

        assertEquals("Error de negocio", ex.getMessage());
        verify(pedidoRequestMapper).toPedido(dto, clienteId);
        verify(pedidoServicePort).crear(pedidoMock, token);
    }
}
