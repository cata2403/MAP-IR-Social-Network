package com.ubb.infrastructure;

import com.ubb.domain.Entity;

public interface DatabaseDataTransfer<ID, E extends Entity<ID>> extends DataTransferStrategy<ID, E> {
}
