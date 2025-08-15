package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.configuration;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.api.OrderTraceServicePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.spi.OrderTracePersistencePort;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.usecase.OrderTraceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
        @Bean
        public OrderTraceServicePort orderTraceServicePort(OrderTracePersistencePort persistencePort) {
            return new OrderTraceUseCase(persistencePort);
        }
    }


