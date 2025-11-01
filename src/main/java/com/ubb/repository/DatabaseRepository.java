package com.ubb.repository;

import com.ubb.domain.Entity;
import java.util.List;

public class DatabaseRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    @Override
    public void add(E entity) {
    }

    @Override
    public E delete(ID id) {
        return null;
    }

    @Override
    public void update(E entity) {
    }

    @Override
    public E get(ID id) {
        return null;
    }

    @Override
    public List<E> getAll() {
        return List.of();
    }
}
