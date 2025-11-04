package com.ubb.business_logic.dtos;

import com.ubb.domain.entity_types.UserType;

public record UserDTO(Long id, UserType userType, String username) {
}
