package com.ubb.infrastructure;
import com.ubb.domain.Entity;

public interface DataTransferStrategy<ID,E extends Entity<ID>> {
    public String serialization(E o);
    public E deserialization(String s);
}
