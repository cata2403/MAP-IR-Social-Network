package com.ubb.repository;

import com.ubb.domain.Entity;
import com.ubb.infrastructure.FileDataTransfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileRepository<ID,E extends Entity<ID>> implements Repository<ID,E> {
    private final File file;
    FileDataTransfer<ID> strategy;
    Map<ID,E> data = new HashMap<>();
    public FileRepository(File file, FileDataTransfer<ID> strategy) {
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
                fw.write(fileLine);
            }
        }
        catch(IOException e) {
            System.out.printf("Error writing to file: %s\n", file.getAbsolutePath());
        }
    }

    private void loadFromFile() throws RepoException, FileNotFoundException {
        if(!file.exists())
            throw new RepoException("<<File does not exist>>");
        data.clear();
        Scanner sc = new  Scanner(file);
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            E entity = (E) strategy.deserialization(line);
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
        E entity = data.get(id);
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
        if(!data.isEmpty())
            return List.copyOf(data.values());
        return new ArrayList<>();
    }

}
