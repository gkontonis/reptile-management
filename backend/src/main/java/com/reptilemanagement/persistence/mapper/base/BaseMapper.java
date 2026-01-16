package com.reptilemanagement.persistence.mapper.base;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.dto.base.BaseDto;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Base mapper interface providing common mapping operations for entities and DTOs.
 * <p>
 * This interface defines standard CRUD mapping methods that are inherited by all entity mappers,
 * following the DRY (Don't Repeat Yourself) principle.
 * <p>
 * The toEntity mapping automatically ignores audit fields (createdAt, createdBy, updatedAt, updatedBy)
 * as these are managed by JPA lifecycle callbacks.
 *
 * @param <E> the entity type that extends BaseEntity
 * @param <D> the DTO type that extends BaseDto
 */
public interface BaseMapper<E extends BaseEntity<?>, D extends BaseDto<?>> {

    /**
     * Converts an entity to its corresponding DTO.
     * <p>
     * Maps all fields including audit fields from the entity to the DTO.
     *
     * @param entity the entity to convert
     * @return the corresponding DTO, or null if the entity is null
     */
    D toDto(E entity);

    /**
     * Converts a DTO to its corresponding entity.
     * <p>
     * Audit fields (createdAt, createdBy, updatedAt, updatedBy) are ignored during mapping
     * as they are managed by JPA @PrePersist and @PreUpdate callbacks.
     *
     * @param dto the DTO to convert
     * @return the corresponding entity, or null if the DTO is null
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    E toEntity(D dto);

    /**
     * Updates an existing entity with values from a DTO.
     * <p>
     * Audit fields (createdAt, createdBy, updatedAt, updatedBy) are ignored during update
     * to preserve original creation data and allow JPA to manage modification timestamps.
     *
     * @param dto the DTO containing the new values
     * @param entity the entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(D dto, @MappingTarget E entity);
}
