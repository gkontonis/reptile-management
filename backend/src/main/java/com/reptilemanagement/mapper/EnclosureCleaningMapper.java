package com.reptilemanagement.mapper;

import com.reptilemanagement.domain.EnclosureCleaning;
import com.reptilemanagement.dto.EnclosureCleaningDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between EnclosureCleaning entities and DTOs.
 * Uses MapStruct for automatic mapping implementation generation.
 */
@Mapper(componentModel = "spring")
public interface EnclosureCleaningMapper {
    /**
     * Converts an EnclosureCleaning entity to an EnclosureCleaningDto.
     * @param enclosureCleaning the entity to convert
     * @return the corresponding DTO
     */
    EnclosureCleaningDto toDto(EnclosureCleaning enclosureCleaning);

    /**
     * Converts an EnclosureCleaningDto to an EnclosureCleaning entity.
     * @param enclosureCleaningDto the DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EnclosureCleaning toEntity(EnclosureCleaningDto enclosureCleaningDto);
}