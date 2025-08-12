package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Pedido;

public interface PedidoServicePort {
    Pedido crear(Pedido pedido, String bearerToken);
}