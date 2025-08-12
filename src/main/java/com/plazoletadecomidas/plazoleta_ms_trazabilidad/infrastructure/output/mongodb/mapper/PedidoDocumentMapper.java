package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.mapper;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.DetallePedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.EstadoPedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Pedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document.DetallePedidoDocument;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document.PedidoDocument;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoDocumentMapper {

    public PedidoDocument toDocument(Pedido pedido) {
        List<DetallePedidoDocument> detalles = pedido.getDetalles().stream()
                .map(d -> new DetallePedidoDocument(d.getPlatoId(), d.getCantidad()))
                .toList();
        return new PedidoDocument(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getRestauranteId(),
                detalles,
                pedido.getEstado().name(),
                pedido.getFechaCreacion()
        );
    }

    public Pedido toPedido(PedidoDocument document) {
        List<DetallePedido> detalles = document.getDetalles().stream()
                .map(d -> {
                    DetallePedido det = new DetallePedido();
                    det.setPlatoId(d.getPlatoId());
                    det.setCantidad(d.getCantidad());
                    return det;
                }).toList();
        Pedido pedido = new Pedido();
        pedido.setId(document.getId());
        pedido.setClienteId(document.getClienteId());
        pedido.setRestauranteId(document.getRestauranteId());
        pedido.setDetalles(detalles);
        pedido.setEstado(EstadoPedido.valueOf(document.getEstado()));
        pedido.setFechaCreacion(document.getFechaCreacion());
        return pedido;
    }

}
