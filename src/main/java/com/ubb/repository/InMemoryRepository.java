package com.ubb.repository;

import com.ubb.domain.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E>{

    private final Map<ID, E> data = new HashMap<>();

    @Override
    public void add(E entity) {

        if( data.containsKey(entity.getId()) ){
            throw new RepoException("<<Entity already exists>>");
        }

        data.put(entity.getId(), entity);
    }

    @Override
    public E delete(ID id) {

        if(!data.containsKey(id)){
            throw new RepoException("<<Entity doesn't exists>>");
        }

        return data.remove(id);
    }

    @Override
    public void update(E entity) {

        if(!data.containsKey(entity.getId())){
            throw new RepoException("<<Entity doesn't exists>>");
        }

        data.put(entity.getId(), entity);
    }

    @Override
    public E get(ID id) {

        if(!data.containsKey(id)){
            throw new RepoException("<<Entity doesn't exists>>");
        }

        return data.get(id);
    }

    @Override
    public List<E> getAll() {
        return new ArrayList<>(data.values());
    }
}
