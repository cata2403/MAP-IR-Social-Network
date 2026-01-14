package com.ubb.domain_layer.validation;

import com.ubb.domain_layer.entities.Entity;

public interface ValidationStrategy {

    public Boolean test(Entity<Long> entity);
}

