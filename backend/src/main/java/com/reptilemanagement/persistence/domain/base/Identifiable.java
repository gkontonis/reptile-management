package com.reptilemanagement.persistence.domain.base;

import java.io.Serializable;

/**
 * Interface for entities that have an identifiable primary key.
 *
 * @param <T> The type of the entity's primary key
 */
public interface Identifiable<T> extends Serializable {
    
    /**
     * Returns the primary key of this entity.
     *
     * @return the entity's primary key
     */
    T getId();

    /**
     * Sets the primary key of this entity.
     *
     * @param id the primary key to set
     */
    void setId(T id);
}
