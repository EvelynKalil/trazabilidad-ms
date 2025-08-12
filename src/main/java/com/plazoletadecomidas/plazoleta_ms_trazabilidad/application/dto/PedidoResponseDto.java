package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponseDto(
        UUID id,
        UUID clienteId,
        UUID restauranteId,
        List<ItemDto> items,
        String estado,
        LocalDateTime fechaCreacion
) {}
