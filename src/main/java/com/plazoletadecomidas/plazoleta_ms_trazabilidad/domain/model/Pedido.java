package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Pedido {
    private UUID id;
    private UUID clienteId;
    private UUID restauranteId;
    private List<DetallePedido> detalles;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;


}
