package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.ItemDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoRequestDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoResponseDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler.PedidoHandler;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.security.AuthValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PedidoControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoHandler pedidoHandler;

    @MockBean
    private AuthValidator authValidator;

    @Test
    @DisplayName("Debería rechazar pedidos con platos repetidos")
    void crearPedido_platosRepetidos_devuelve400() throws Exception {
        PedidoRequestDto dto = new PedidoRequestDto(
                UUID.randomUUID(),
                List.of(
                        new ItemDto(UUID.randomUUID(), 2),
                        new ItemDto(UUID.randomUUID(), 2),
                        new ItemDto(UUID.randomUUID(), 1)
                )
        );

        when(authValidator.validate(anyString(), any(Role.class)))
                .thenReturn(UUID.randomUUID());

        when(pedidoHandler.crearPedido(any(PedidoRequestDto.class), any(UUID.class), anyString()))
                .thenThrow(new IllegalArgumentException("No se permiten platos repetidos"));

        mockMvc.perform(post("/pedidos")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No se permiten platos repetidos"));
    }

    @Test
    @DisplayName("Debería crear pedido exitosamente")
    void crearPedido_valido_devuelve201() throws Exception {
        UUID clienteId = UUID.randomUUID();
        PedidoRequestDto dto = new PedidoRequestDto(
                clienteId,
                List.of(
                        new ItemDto(UUID.randomUUID(), 2),
                        new ItemDto(UUID.randomUUID(), 1)
                )
        );

        PedidoResponseDto response = new PedidoResponseDto(
                UUID.randomUUID(),
                clienteId,
                dto.restauranteId(),
                dto.items(),
                "PENDIENTE",
                LocalDateTime.now()
        );

        when(authValidator.validate(anyString(), any(Role.class)))
                .thenReturn(clienteId);

        when(pedidoHandler.crearPedido(any(PedidoRequestDto.class), any(UUID.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/pedidos")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }
}
