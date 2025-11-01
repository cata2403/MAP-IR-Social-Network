package com.ubb.business_logic.dtos;

import com.ubb.domain.UserType;
import com.ubb.domain.User;

public record LoginDTO(Boolean confirmation, UserType userType, User user) {
}
