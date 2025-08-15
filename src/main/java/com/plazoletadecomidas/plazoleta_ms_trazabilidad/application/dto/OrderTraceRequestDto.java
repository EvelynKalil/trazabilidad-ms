package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderTraceRequestDto {
    private UUID orderId;
    private String status;
    private UUID customerId;
    private LocalDateTime changeTime;
    private UUID restaurantId;
}
