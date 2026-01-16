package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.Enclosure;
import com.reptilemanagement.persistence.dto.EnclosureDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Enclosure entities and DTOs.
 * Uses MapStruct for automatic mapping implementation generation.
 */
@Mapper(componentModel = "spring")
public interface EnclosureMapper {
    /**
     * Converts an Enclosure entity to an EnclosureDto.
     * @param enclosure the entity to convert
     * @return the corresponding DTO
     */
    EnclosureDto toDto(Enclosure enclosure);

    /**
     * Converts an EnclosureDto to an Enclosure entity.
     * @param enclosureDto the DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Enclosure toEntity(EnclosureDto enclosureDto);
}