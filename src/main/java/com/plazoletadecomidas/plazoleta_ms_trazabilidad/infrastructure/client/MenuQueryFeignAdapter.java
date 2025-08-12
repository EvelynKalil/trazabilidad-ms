package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.client;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.DetallePedido;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.MenuQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuQueryFeignAdapter implements MenuQueryPort {

    private final PlazoletaClient client;

    @Override
    public void validarPlatosDelRestaurante(UUID restauranteId, List<DetallePedido> detalles, String bearerToken) {
        PlazoletaClient.DishesPageResponse page = client.getDishesByRestaurant(
                restauranteId.toString(), 0, 100, bearerToken // 🔹 Enviamos token
        );

        Set<String> platosValidos = page.content().stream()
                .map(PlazoletaClient.DishDto::id)
                .collect(Collectors.toSet());

        for (DetallePedido detalle : detalles) {
            if (!platosValidos.contains(detalle.getPlatoId().toString())) {
                throw new IllegalArgumentException(
                        "El plato con ID " + detalle.getPlatoId() +
                                " no pertenece al restaurante " + restauranteId
                );
            }
        }
    }
}

