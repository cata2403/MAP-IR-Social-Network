package com.ubb.infrastructure;
import com.ubb.domain.Entity;

public interface DataTransferStrategy<ID, E extends Entity<ID>> {

    public String serialize(E o);

    public E deserialize(String s);
}
