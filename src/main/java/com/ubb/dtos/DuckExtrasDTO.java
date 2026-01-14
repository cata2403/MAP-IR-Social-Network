package com.ubb.dtos;

import com.ubb.domain_layer.enums.DuckType;

public record DuckExtrasDTO(double speed, double resistance, DuckType type) {
}

