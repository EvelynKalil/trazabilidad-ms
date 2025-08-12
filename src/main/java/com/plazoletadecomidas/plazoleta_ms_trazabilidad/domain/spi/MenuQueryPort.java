package com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.DetallePedido;
import java.util.List;
import java.util.UUID;

public interface MenuQueryPort {
    void validarPlatosDelRestaurante(UUID restauranteId, List<DetallePedido> detalles, String bearerToken);
}
