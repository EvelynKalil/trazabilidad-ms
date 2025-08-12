package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "plazoleta-ms", url = "${plazoleta.url}")
public interface PlazoletaClient {

    @GetMapping("/dishes/{restaurantId}")
    DishesPageResponse getDishesByRestaurant(
            @PathVariable("restaurantId") String restaurantId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestHeader("Authorization") String bearer // 🔹 Nuevo
    );

    record DishesPageResponse(List<DishDto> content) {}
    record DishDto(String id, String restaurantId) {}
}
