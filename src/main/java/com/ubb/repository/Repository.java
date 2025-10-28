package com.ubb.repository;
import com.ubb.domain.Entity;
import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {
    public void add(E entity);
    public E delete(ID id);
    public void update(E entity);
    public E get(ID id);
    public List<E> getAll();
}
