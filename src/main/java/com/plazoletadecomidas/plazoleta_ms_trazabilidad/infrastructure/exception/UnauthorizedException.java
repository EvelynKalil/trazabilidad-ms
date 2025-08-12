package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
