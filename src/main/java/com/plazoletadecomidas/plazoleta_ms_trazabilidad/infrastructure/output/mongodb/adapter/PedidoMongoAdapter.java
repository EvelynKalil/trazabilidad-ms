package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.adapter;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Pedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.PedidoPersistencePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document.PedidoDocument;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.mapper.PedidoDocumentMapper;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.repository.PedidoMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PedidoMongoAdapter implements PedidoPersistencePort {

    private final PedidoMongoRepository pedidoMongoRepository;
    private final PedidoDocumentMapper pedidoDocumentMapper;

    @Override
    public boolean existePedidoActivo(UUID clienteId) {
        return pedidoMongoRepository.existsByClienteIdAndEstadoIn(
                clienteId, List.of("PENDIENTE","EN_PREPARACION","LISTO")
        );
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoDocument document = pedidoDocumentMapper.toDocument(pedido);
        PedidoDocument guardado = pedidoMongoRepository.save(document);
        return pedidoDocumentMapper.toPedido(guardado);
    }
}
