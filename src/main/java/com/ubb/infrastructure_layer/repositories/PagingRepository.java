package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.Entity;
import com.ubb.infrastructure_layer.utils.paging.Page;
import com.ubb.infrastructure_layer.utils.paging.Pageable;

public interface PagingRepository<ID , E extends Entity<ID>> extends Repository<ID, E> {

    Page<E> findAllOnPage(Pageable pageable);
}

