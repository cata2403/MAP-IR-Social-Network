package com.ubb.dtos;

import com.ubb.domain_layer.enums.UserType;

public class UserDTO{
    private Long id;
    private UserType userType;
    private String username;
    public UserDTO(Long id, UserType userType, String username) {
        this.id = id;
        this.userType = userType;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

