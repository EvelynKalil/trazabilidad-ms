package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "pedidos")
public class PedidoDocument {

    @Id
    @Field(targetType = FieldType.BINARY)
    private UUID id;

    @Field(targetType = FieldType.BINARY)
    private UUID clienteId;

    @Field(targetType = FieldType.BINARY)
    private UUID restauranteId;

    private List<DetallePedidoDocument> detalles;

    private String estado;

    private LocalDateTime fechaCreacion;
}
