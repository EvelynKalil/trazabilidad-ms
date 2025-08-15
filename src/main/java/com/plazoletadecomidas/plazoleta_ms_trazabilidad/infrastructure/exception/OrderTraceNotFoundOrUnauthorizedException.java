package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.exception;

public class OrderTraceNotFoundOrUnauthorizedException extends RuntimeException {
    public OrderTraceNotFoundOrUnauthorizedException(String message) {
        super(message);
    }
}
