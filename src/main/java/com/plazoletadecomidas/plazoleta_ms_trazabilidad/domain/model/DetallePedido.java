package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DetallePedido {
    private UUID platoId;
    private Integer cantidad;

}