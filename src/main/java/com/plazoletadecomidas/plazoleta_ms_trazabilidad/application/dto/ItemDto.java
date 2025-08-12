package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

public record ItemDto(
        @NotNull UUID platoId,
        @NotNull @Positive Integer cantidad
) {}
