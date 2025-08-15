package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderTraceRequestDto {
    private UUID orderId;
    private String status;
    private UUID customerId;
    private LocalDateTime changeTime;
    private UUID restaurantId;
}
