package com.ubb.business_logic.dtos;

import com.ubb.domain.entity_types.UserType;
import com.ubb.domain.entities.User;

public record LoginDTO(Boolean confirmation, UserType userType, User user) {
}
