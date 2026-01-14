package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.RaceEvent;

import java.util.List;

public interface EventRepo extends Repository<Long, RaceEvent>{
    public List<Long> getSpectators(Long id);
    public List<Long> getParticipants(Long id);
}

