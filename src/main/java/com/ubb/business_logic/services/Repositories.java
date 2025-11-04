package com.ubb.business_logic.services;

import com.ubb.domain.entities.*;
import com.ubb.repository.Repository;

public record Repositories(
        Repository<Long, Person> personRepository,
        Repository<Long, Duck> duckRepository,
        Repository<Long, Friendship> friendshipRepository,
        Repository<Long, SwimMasters> flockRepository,
        Repository<Long, Event> eventRepository
) {}
