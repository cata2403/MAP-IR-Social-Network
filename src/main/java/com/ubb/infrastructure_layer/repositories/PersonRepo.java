package com.ubb.infrastructure_layer.repositories;
import com.ubb.domain_layer.entities.Person;

import java.util.Optional;

public interface PersonRepo extends Repository<Long, Person>{

    public Optional<Person> findByUsername(String username);
}

