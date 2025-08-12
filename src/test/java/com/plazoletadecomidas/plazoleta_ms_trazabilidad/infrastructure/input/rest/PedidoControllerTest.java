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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false) // Deshabilitamos filtros de seguridad reales
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoHandler pedidoHandler;

    @MockBean
    private AuthValidator authValidator;

    @Test
    @DisplayName("Debe crear un pedido exitosamente (201)")
    void crearPedido_ok() throws Exception {
        UUID clienteId = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();
        String token = "Bearer faketoken";

        PedidoRequestDto requestDto = new PedidoRequestDto(
                restauranteId,
                List.of(new ItemDto(UUID.randomUUID(), 2))
        );

        PedidoResponseDto responseDto = new PedidoResponseDto(
                UUID.randomUUID(),
                clienteId,
                restauranteId,
                requestDto.items(),
                "PENDIENTE",
                LocalDateTime.now()
        );

        when(authValidator.validate(token, Role.CLIENTE)).thenReturn(clienteId);
        when(pedidoHandler.crearPedido(any(PedidoRequestDto.class), eq(clienteId), anyString()))

                .thenReturn(responseDto);

        mockMvc.perform(post("/pedidos")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.clienteId").value(clienteId.toString()))
                .andExpect(jsonPath("$.restauranteId").value(restauranteId.toString()));
    }

    @Test
    @DisplayName("Debe devolver 400 si hay error de negocio")
    void crearPedido_errorNegocio() throws Exception {
        UUID clienteId = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();
        String token = "Bearer faketoken";

        PedidoRequestDto requestDto = new PedidoRequestDto(
                restauranteId,
                List.of(new ItemDto(UUID.randomUUID(), 0)) // cantidad inválida
        );

        when(authValidator.validate(token, Role.CLIENTE)).thenReturn(clienteId);
        when(pedidoHandler.crearPedido(any(PedidoRequestDto.class), eq(clienteId), anyString()))
                .thenThrow(new IllegalArgumentException("Cada ítem debe tener cantidad > 0"));

        mockMvc.perform(post("/pedidos")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cada ítem debe tener cantidad > 0"));
    }
}
