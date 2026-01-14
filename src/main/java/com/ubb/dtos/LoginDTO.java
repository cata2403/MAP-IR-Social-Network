package com.ubb.dtos;

import com.ubb.domain_layer.entities.User;
import com.ubb.domain_layer.enums.UserType;

public record LoginDTO(Boolean confirmation, UserType userType, User user) {
}

