package com.reptilemanagement.persistence.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base DTO class for data transfer objects requiring audit fields.
 * <p>
 * Provides common audit fields for all entity DTOs:
 * <ul>
 *   <li>{@code createdAt} - Timestamp when entity was created</li>
 *   <li>{@code createdBy} - User who created the entity</li>
 *   <li>{@code updatedAt} - Timestamp when entity was last modified</li>
 *   <li>{@code updatedBy} - User who last modified the entity</li>
 * </ul>
 * <p>
 * Subclasses must implement {@link #getId()} and {@link #setId(Serializable)} for the primary key.
 *
 * @param <T> The type of the DTO's identifier (e.g., Long, Integer, UUID)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto<T> implements Serializable {

    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Returns the identifier of this DTO.
     *
     * @return the DTO's identifier
     */
    public abstract T getId();

    /**
     * Sets the identifier of this DTO.
     *
     * @param id the identifier to set
     */
    public abstract void setId(T id);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseDto<?> that = (BaseDto<?>) o;
        if (getId() == null || that.getId() == null) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? Objects.hash(getId()) : super.hashCode();
    }
}
