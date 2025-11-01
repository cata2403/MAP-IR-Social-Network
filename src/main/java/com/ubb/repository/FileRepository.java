package com.ubb.repository;

import com.ubb.domain.Entity;
import com.ubb.infrastructure.DataTransferStrategy;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class FileRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final File dataFile;
    DataTransferStrategy<ID, E> strategy;
    Map<ID, E> data = new HashMap<>();

    public FileRepository(File file, DataTransferStrategy<ID, E> strategy) throws RepoException{

        this.dataFile = file;
        this.strategy = strategy;
        loadFromFile();
    }

    private void saveToFile() throws RepoException {

        if( !dataFile.exists() )
            throw new RepoException("<<File does not exist>>");

        try(FileWriter fw = new FileWriter(dataFile)) {

            for(Map.Entry<ID,E> entry : data.entrySet()) {
                String fileLine = strategy.serialize(entry.getValue());
                fw.write(fileLine + System.lineSeparator());
            }
        }
        catch(Exception error) {
            throw new RepoException("<<Failed to write to file>>");
        }
    }

    private void loadFromFile() throws RepoException{

        if( !dataFile.exists() )
            throw new RepoException("<<File does not exist>>");

        data.clear();
        try {
            Scanner sc = new Scanner(dataFile);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                E entity = strategy.deserialize(line);
                data.put(entity.getId(), entity);
            }
        }
        catch(Exception error) {
            throw new RepoException("<<Failed to read from file>>");
        }
    }

    @Override
    public void add(E entity) throws RepoException {

        if( data.containsKey(entity.getId()) )
            throw new RepoException(
                    "<<Entity with id " + entity.getId().toString() + " already exists>>"
            );

        data.put(entity.getId(), entity);
        saveToFile();
    }

    @Override
    public E delete(ID id) throws RepoException {

        E entity = data.remove(id);

        if(entity == null)
            throw new RepoException(
                    "<<Entity with id " + id.toString() + " does not exist>>"
            );

        saveToFile();
        return entity;
    }

    @Override
    public void update(E entity) throws RepoException {

        if( !data.containsKey(entity.getId()) )
            throw new RepoException(
                    "<<Entity with id " + entity.getId().toString() + " does not exist>>"
            );

        data.put(entity.getId(), entity);
        saveToFile();
    }

    @Override
    public E get(ID id) throws RepoException {

        if( !data.containsKey(id) )
            throw new RepoException(
                    "<<Entity with id " + id.toString() + " does not exist>>"
            );

        return data.get(id);
    }

    @Override
    public List<E> getAll() {
        return new ArrayList<>(data.values());
    }
}
