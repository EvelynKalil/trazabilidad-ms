package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Pedido;
import java.util.UUID;

public interface PedidoPersistencePort {
    boolean existePedidoActivo(UUID clienteId);     // PENDIENTE/EN_PREPARACION/LISTO
    Pedido guardar(Pedido pedido);
}