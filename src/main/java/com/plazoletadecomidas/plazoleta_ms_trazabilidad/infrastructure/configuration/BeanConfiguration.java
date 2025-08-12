package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.configuration;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api.PedidoServicePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.MenuQueryPort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.PedidoPersistencePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.usecase.PedidoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public PedidoServicePort pedidoServicePort(PedidoPersistencePort persistence,
                                               MenuQueryPort menuQueryPort) {
        return new PedidoUseCase(persistence, menuQueryPort);
    }
}
