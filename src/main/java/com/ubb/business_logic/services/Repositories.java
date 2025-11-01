package com.ubb.business_logic.services;

import com.ubb.domain.Duck;
import com.ubb.domain.Friendship;
import com.ubb.domain.Person;
import com.ubb.repository.Repository;

public record Repositories(
        Repository<Long, Person> personRepository,
        Repository<Long, Duck> duckRepository,
        Repository<Long, Friendship> friendshipRepository
) {}
