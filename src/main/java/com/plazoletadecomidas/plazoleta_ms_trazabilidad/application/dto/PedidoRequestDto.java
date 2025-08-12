package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PedidoRequestDto(
        @NotNull UUID restauranteId,
        @NotNull List<ItemDto> items
) {}
