package com.plazoletadecomidas.plazoleta_ms_trazabilidad.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEfficiencyDto {
    private String employeeId;
    private double avgMinutes;
}

