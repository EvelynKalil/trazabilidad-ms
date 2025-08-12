package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.input.rest;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoRequestDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto.PedidoResponseDto;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.handler.PedidoHandler;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.security.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoHandler pedidoHandler;
    private final AuthValidator authValidator;

    @PostMapping
    public ResponseEntity<PedidoResponseDto> crearPedido(
            @Valid @RequestBody PedidoRequestDto dto,
            @RequestHeader("Authorization") String token
    ) {
        UUID clienteId = authValidator.validate(token, Role.CLIENTE);
        PedidoResponseDto pedidoCreado = pedidoHandler.crearPedido(dto, clienteId, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreado);
    }


}
