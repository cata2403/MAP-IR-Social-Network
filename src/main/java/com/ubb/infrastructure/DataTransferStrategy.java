package com.ubb.infrastructure;
import com.ubb.domain.Entity;

public interface DataTransferStrategy<ID> {
    public String serialization(Entity<ID> o);
    public Entity<ID> deserialization(String s);
}
