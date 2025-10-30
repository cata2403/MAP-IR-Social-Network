package com.ubb.business_logic.dtos;

import com.ubb.domain.UserType;

public class UserDTO {
    private final Long id;
    private final UserType userType;
    private final String username;
    public UserDTO(Long id, UserType userType, String username) {
        this.userType = userType;
        this.username = username;
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public UserType getUserType() {
        return userType;
    }
    public String getUsername() {
        return username;
    }
}
