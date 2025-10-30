package com.ubb.business_logic.dtos;

import com.ubb.domain.UserType;
import com.ubb.domain.User;

public class LoginDTO {
    private final Boolean confirmation;
    private final UserType userType;
    private final User user;
    public LoginDTO(Boolean confirmation, UserType userType, User user) {
        this.confirmation = confirmation;
        this.userType = userType;
        this.user = user;
    }
    public Boolean getConfirmation() {
        return confirmation;
    }
    public UserType getUserType() {
        return userType;
    }
    public User getUser() {
        return user;
    }
}
