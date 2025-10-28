package com.ubb.repository;
import com.ubb.domain.Entity;
import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {
    public void add(E entity) throws RepoException;
    public E delete(ID id) throws RepoException;
    public void update(E entity) throws RepoException;
    public E get(ID id) throws RepoException;
    public List<E> getAll();
}
