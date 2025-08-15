package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto;

import java.util.UUID;

public record OrderEfficiencyDto(UUID orderId, long totalMinutes) {}
