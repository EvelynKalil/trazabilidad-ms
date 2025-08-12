package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.output.mongodb.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDocument {

    @Field(targetType = FieldType.BINARY)
    private UUID platoId;

    private Integer cantidad;
}
