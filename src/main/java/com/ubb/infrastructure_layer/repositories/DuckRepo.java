package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.Duck;
import com.ubb.domain_layer.entities.Person;
import com.ubb.dtos.DuckFilterDTO;
import com.ubb.infrastructure_layer.utils.paging.Page;
import com.ubb.infrastructure_layer.utils.paging.Pageable;

import java.util.Optional;

public interface DuckRepo extends Repository<Long, Duck>, PagingRepository<Long, Duck> {

    Page<Duck> findAllOnPage(Pageable pageable, DuckFilterDTO filter);
    Optional<Duck> findByUsername(String username);
}

