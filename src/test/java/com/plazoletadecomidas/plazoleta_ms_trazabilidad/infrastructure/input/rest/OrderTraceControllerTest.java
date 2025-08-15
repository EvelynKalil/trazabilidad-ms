package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.EmployeeEfficiencyDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.OrderEfficiencyDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.OrderTraceRequestDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler.OrderTraceHandler;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.OrderTrace;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.security.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderTraceController.class)
class OrderTraceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderTraceHandler handler;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthValidator authValidator;

    @BeforeEach
    void setUp() {
        when(authValidator.validate(anyString(), any())).thenReturn(UUID.randomUUID());
        when(authValidator.getRoleFromToken(anyString())).thenReturn("PROPIETARIO");
    }

    @Test
    void getOrderTrace_shouldReturnOrderTrace() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderTrace trace = new OrderTrace(orderId, UUID.randomUUID(), UUID.randomUUID(), List.of());
        when(handler.getOrderTrace(eq(orderId), anyString())).thenReturn(trace);

        mockMvc.perform(get("/trazabilidad/orders/{orderId}", orderId)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId.toString()));
    }

    @Test
    void efficiencyOrders_shouldReturnList() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        List<OrderEfficiencyDto> mockList = List.of(new OrderEfficiencyDto(UUID.randomUUID(), 15L));
        when(handler.efficiencyOrders(eq(restaurantId), anyString())).thenReturn(mockList);

        mockMvc.perform(get("/trazabilidad/efficiency/orders")
                        .param("restaurantId", restaurantId.toString())
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].minutes").value(15));
    }

    @Test
    void efficiencyByEmployee_shouldReturnList() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        List<EmployeeEfficiencyDto> mockList = List.of(new EmployeeEfficiencyDto("emp1", 20.0));
        when(handler.efficiencyByEmployee(eq(restaurantId), anyString())).thenReturn(mockList);

        mockMvc.perform(get("/trazabilidad/efficiency/employees")
                        .param("restaurantId", restaurantId.toString())
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value("emp1"))
                .andExpect(jsonPath("$[0].avgMinutes").value(20.0));
    }

    @Test
    void saveOrderTrace_shouldCallHandler() throws Exception {
        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        OrderTraceRequestDto dto = OrderTraceRequestDto.builder()
                .orderId(orderId)
                .status("PENDIENTE")
                .customerId(customerId)
                .changeTime(LocalDateTime.now())
                .restaurantId(restaurantId)
                .build();

        mockMvc.perform(post("/trazabilidad/orders")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Mockito.verify(handler).saveOrderTrace(eq(dto), anyString());
    }

}
