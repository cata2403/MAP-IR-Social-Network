package com.ubb.infrastructure_layer.repositories;
import com.ubb.domain_layer.entities.Entity;
import com.ubb.exceptions.RepoException;
import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {

    void add(E entity) throws RepoException;

    Optional<E> delete(ID id) throws RepoException;

    void update(E entity) throws RepoException;

    Optional<E> get(ID id) throws RepoException;

    Iterable<E> getAll();
}

