package com.reptilemanagement.persistence.domain.base;

/**
 * Interface for entities that can be updated from a DTO.
 * <p>
 * Implementing entities should provide an update method that copies
 * relevant fields from the provided DTO to update the entity's state.
 *
 * @param <D> The DTO type to update from
 */
public interface EntityUpdatable<D> {
    /**
     * Updates this entity's fields from the provided DTO.
     * Only updates business fields, not audit fields or relationships
     * (unless specifically needed).
     *
     * @param dto the DTO containing the new values
     */
    void update(D dto);
}
