package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api.PedidoServicePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.DetallePedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.EstadoPedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Pedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.MenuQueryPort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.PedidoPersistencePort;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PedidoUseCase implements PedidoServicePort {

    private final PedidoPersistencePort persistence;
    private final MenuQueryPort menuQuery;

    public PedidoUseCase(PedidoPersistencePort p, MenuQueryPort q) {
        this.persistence = p;
        this.menuQuery = q;
    }

    @Override
    public Pedido crear(Pedido pedido, String bearerToken) {

        // Validar que existan ítems
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener ítems");
        }

        // Validar que cada ítem tenga cantidad > 0
        pedido.getDetalles().forEach(d -> {
            if (d.getCantidad() == null || d.getCantidad() <= 0) {
                throw new IllegalArgumentException("Cada ítem debe tener cantidad > 0");
            }
        });

        // 🚨 Nueva validación: evitar platos duplicados
        Set<UUID> platosUnicos = new HashSet<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (!platosUnicos.add(detalle.getPlatoId())) {
                throw new IllegalArgumentException("No se puede repetir el mismo plato en un pedido");
            }
        }

        // Validar que el cliente no tenga pedidos activos
        if (persistence.existePedidoActivo(pedido.getClienteId())) {
            throw new IllegalStateException("El cliente ya tiene un pedido en proceso");
        }

        // Validar que los platos pertenezcan al restaurante
        menuQuery.validarPlatosDelRestaurante(pedido.getRestauranteId(), pedido.getDetalles(), bearerToken);

        // Setear estado y fecha
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());

        return persistence.guardar(pedido);
    }
}
