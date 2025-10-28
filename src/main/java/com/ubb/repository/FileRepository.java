package com.ubb.repository;

import com.ubb.domain.Entity;
import com.ubb.infrastructure.FileDataTransfer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileRepository<ID,E extends Entity<ID>> implements Repository<ID,E> {
    private File file;
    FileDataTransfer strategy;
    Map<ID,E> data = new HashMap<ID,E>();
    public FileRepository(File file, FileDataTransfer strategy) {
        this.file = file;
        this.strategy = strategy;
    }

    private void saveToFile() {

    }

    private void loadFromFile() {

    }

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
