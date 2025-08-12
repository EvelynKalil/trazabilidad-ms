package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.*;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.MenuQueryPort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.PedidoPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoUseCaseTest {

    private PedidoPersistencePort persistence;
    private MenuQueryPort menuQuery;
    private PedidoUseCase useCase;

    @BeforeEach
    void setUp() {
        persistence = mock(PedidoPersistencePort.class);
        menuQuery = mock(MenuQueryPort.class);
        useCase = new PedidoUseCase(persistence, menuQuery);
    }

    @Test
    void crearPedido_conPlatosDuplicados_lanzaExcepcion() {
        UUID platoId = UUID.randomUUID();
        Pedido pedido = Pedido.builder()
                .clienteId(UUID.randomUUID())
                .restauranteId(UUID.randomUUID())
                .detalles(List.of(
                        new DetallePedido(platoId, 2),
                        new DetallePedido(platoId, 1)
                ))
                .build();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.crear(pedido, "token")
        );

        assertEquals("No se puede repetir el mismo plato en un pedido", ex.getMessage());
    }

    @Test
    void crearPedido_clienteConPedidoActivo_lanzaExcepcion() {
        when(persistence.existePedidoActivo(any())).thenReturn(true);

        Pedido pedido = Pedido.builder()
                .clienteId(UUID.randomUUID())
                .restauranteId(UUID.randomUUID())
                .detalles(List.of(new DetallePedido(UUID.randomUUID(), 1)))
                .build();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> useCase.crear(pedido, "token")
        );

        assertEquals("El cliente ya tiene un pedido en proceso", ex.getMessage());
    }

    @Test
    void crearPedido_valido_guardaEnRepositorio() {
        Pedido pedido = Pedido.builder()
                .clienteId(UUID.randomUUID())
                .restauranteId(UUID.randomUUID())
                .detalles(List.of(new DetallePedido(UUID.randomUUID(), 1)))
                .build();

        when(persistence.guardar(any())).thenAnswer(i -> {
            Pedido p = i.getArgument(0);
            p.setId(UUID.randomUUID());
            p.setFechaCreacion(LocalDateTime.now());
            return p;
        });

        Pedido resultado = useCase.crear(pedido, "token");

        assertNotNull(resultado.getId());
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
        verify(persistence).guardar(any());
    }
}
