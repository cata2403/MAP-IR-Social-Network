package com.ubb.domain_layer.entities;

public abstract class Entity<ID> {

    private final ID id;

    /**
     * Creates instance of an Entity
     *
     * @param id unique information to identify an entity
     */
    public Entity(ID id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier of the entity
     *
     * @return the id of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * Compares if the entities are the same
     * @param entity the entity the instance is compared to
     * @return true - entities are the same, false - otherwise
     */
    public boolean equals(Entity<ID> entity){
        return id.equals(entity.id);
    }
}
