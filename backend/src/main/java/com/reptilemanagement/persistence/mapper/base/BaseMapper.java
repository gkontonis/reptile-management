package com.reptilemanagement.persistence.mapper.base;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.dto.base.BaseDto;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Map;

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
     * Converts an entity to its corresponding DTO with conditional mapping.
     * <p>
     * Maps all fields including audit fields from the entity to the DTO.
     * The conditions map can be used by custom mappers to control relationship mapping.
     *
     * @param entity the entity to convert
     * @param conditions optional conditions to control mapping behavior (can be null)
     * @return the corresponding DTO, or null if the entity is null
     */
    default D toDto(E entity, Map<String, Boolean> conditions) {
        return toDto(entity);
    }

    /**
     * Converts a list of entities to DTOs.
     *
     * @param entities the list of entities to convert
     * @return the corresponding list of DTOs
     */
    List<D> toDtoList(List<E> entities);

    /**
     * Converts a list of entities to DTOs with conditional mapping.
     *
     * @param entities the list of entities to convert
     * @param conditions optional conditions to control mapping behavior (can be null)
     * @return the corresponding list of DTOs
     */
    default List<D> toDtoList(List<E> entities, Map<String, Boolean> conditions) {
        return toDtoList(entities);
    }

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
     * Converts a DTO to its corresponding entity with conditional mapping.
     * <p>
     * Audit fields (createdAt, createdBy, updatedAt, updatedBy) are ignored during mapping
     * as they are managed by JPA @PrePersist and @PreUpdate callbacks.
     * The conditions map can be used by custom mappers to control relationship mapping.
     *
     * @param dto the DTO to convert
     * @param conditions optional conditions to control mapping behavior (can be null)
     * @return the corresponding entity, or null if the DTO is null
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    default E toEntity(D dto, Map<String, Boolean> conditions) {
        return toEntity(dto);
    }

    /**
     * Converts a list of DTOs to entities.
     *
     * @param dtos the list of DTOs to convert
     * @return the corresponding list of entities
     */
    List<E> toEntityList(List<D> dtos);

    /**
     * Converts a list of DTOs to entities with conditional mapping.
     *
     * @param dtos the list of DTOs to convert
     * @param conditions optional conditions to control mapping behavior (can be null)
     * @return the corresponding list of entities
     */
    default List<E> toEntityList(List<D> dtos, Map<String, Boolean> conditions) {
        return toEntityList(dtos);
    }

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

