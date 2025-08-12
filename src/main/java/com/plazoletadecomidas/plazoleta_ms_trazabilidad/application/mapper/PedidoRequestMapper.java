package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.mapper;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.ItemDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoRequestDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoResponseDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.DetallePedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Pedido;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class PedidoRequestMapper {

    public Pedido toPedido(PedidoRequestDto dto, UUID clienteId) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(clienteId);
        pedido.setRestauranteId(dto.restauranteId());
        pedido.setDetalles(dto.items().stream()
                .map(item -> {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setPlatoId(item.platoId());
                    detalle.setCantidad(item.cantidad());
                    return detalle;
                })
                .toList());
        return pedido;
    }

    public PedidoResponseDto toResponseDto(Pedido pedido) {
        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getRestauranteId(),
                pedido.getDetalles().stream()
                        .map(d -> new ItemDto(d.getPlatoId(), d.getCantidad()))
                        .toList(),
                pedido.getEstado().name(),
                pedido.getFechaCreacion()
        );
    }
}

