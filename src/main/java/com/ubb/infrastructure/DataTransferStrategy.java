package com.ubb.infrastructure;

public interface DataTransferStrategy {
    public String serialization(Object o);
    public Object deserialization(String s);
}
