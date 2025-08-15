package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.input.rest;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler.OrderTraceHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderTraceController.class)
class OrderTraceControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderTraceHandler handler; // <- Mock obligatorio para que el controller cargue

    @Test
    void getOrderTrace_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/trazabilidad/orders/{orderId}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void efficiencyOrders_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/trazabilidad/efficiency/orders")
                        .param("restaurantId", UUID.randomUUID().toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void efficiencyByEmployee_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/trazabilidad/efficiency/employees")
                        .param("restaurantId", UUID.randomUUID().toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void saveOrderTrace_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(post("/trazabilidad/orders")
                        .with(csrf())) // Simula token CSRF válido
                .andExpect(status().isUnauthorized());
    }
}
