package com.ubb.repository;

import com.ubb.domain.Entity;
import com.ubb.infrastructure.DataTransferStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileRepository<ID,E extends Entity<ID>> implements Repository<ID,E> {
    private final File file;
    DataTransferStrategy<ID,E> strategy;
    Map<ID,E> data = new HashMap<>();
    public FileRepository(File file, DataTransferStrategy<ID,E> strategy) {
        this.file = file;
        this.strategy = strategy;
        try{loadFromFile();}
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void saveToFile() throws RepoException {
        if(!file.exists())
            throw new RepoException("<<File does not exist>>");
        try(FileWriter fw = new FileWriter(file)) {
            for(Map.Entry<ID,E> entry : data.entrySet()) {
                String fileLine = strategy.serialization(entry.getValue());
                fw.write(fileLine+System.lineSeparator());
            }
        }
        catch(IOException e) {
            throw new RepoException("<<Failed to write to file>>");
        }
    }

    private void loadFromFile() throws RepoException, FileNotFoundException {
        if(!file.exists())
            throw new RepoException("<<File does not exist>>");
        data.clear();
        Scanner sc = new  Scanner(file);
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            E entity = strategy.deserialization(line);
            data.put(entity.getId(), entity);
        }
    }

    @Override
    public void add(E entity) throws RepoException {
        if(data.containsKey(entity.getId()))
            throw new RepoException("<<Entity with id " + entity.getId().toString() + " already exists>>");
        data.put(entity.getId(), entity);
        saveToFile();
    }

    @Override
    public E delete(ID id) throws RepoException {
        E entity = data.remove(id);
        if (entity == null)
            throw new RepoException("<<Entity with id " + id.toString() + " does not exist>>");
        saveToFile();
        return entity;
    }

    @Override
    public void update(E entity) throws RepoException {
        if(!data.containsKey(entity.getId()))
            throw new RepoException("<<Entity with id " + entity.getId().toString() + " does not exist>>");
        data.put(entity.getId(), entity);
        saveToFile();
    }

    @Override
    public E get(ID id) throws RepoException {
        if(!data.containsKey(id))
            throw new RepoException("<<Entity with id " + id.toString() + " does not exist>>");
        return data.get(id);
    }

    @Override
    public List<E> getAll() {
        return new ArrayList<>(data.values());
    }
}
