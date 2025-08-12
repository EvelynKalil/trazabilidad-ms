package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoRequestDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoResponseDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.mapper.PedidoRequestMapper;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api.PedidoServicePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PedidoHandler {

    private final PedidoServicePort pedidoServicePort;
    private final PedidoRequestMapper pedidoRequestMapper;

    public PedidoResponseDto crearPedido(PedidoRequestDto dto, UUID clienteId, String bearerToken) {
        Pedido pedido = pedidoRequestMapper.toPedido(dto, clienteId);
        return pedidoRequestMapper.toResponseDto(pedidoServicePort.crear(pedido, bearerToken));
    }
}
