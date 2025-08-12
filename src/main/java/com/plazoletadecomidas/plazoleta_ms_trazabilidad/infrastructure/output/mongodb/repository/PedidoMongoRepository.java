package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.repository;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document.PedidoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Collection;
import java.util.UUID;

public interface PedidoMongoRepository extends MongoRepository<PedidoDocument, String> {
    boolean existsByClienteIdAndEstadoIn(UUID clienteId, Collection<String> estados);
}