package com.ubb.business_logic.services;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
