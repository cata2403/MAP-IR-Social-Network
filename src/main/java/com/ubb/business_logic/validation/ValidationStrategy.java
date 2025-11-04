package com.ubb.business_logic.validation;

import com.ubb.domain.entities.Entity;

public interface ValidationStrategy {

    public Boolean test(Entity<Long> entity);
}
