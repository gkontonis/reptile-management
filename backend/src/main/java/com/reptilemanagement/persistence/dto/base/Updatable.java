package com.reptilemanagement.persistence.dto.base;

/**
 * Interface for DTOs that support updating from another DTO instance.
 * <p>
 * Implementing classes should provide an update method that copies
 * relevant fields from the source DTO to the current instance.
 *
 * @param <T> The type of the DTO to update from
 */
public interface Updatable<T> {
    /**
     * Updates this DTO's fields from the provided DTO.
     *
     * @param dto the DTO containing the new values
     */
    void update(T dto);
}
