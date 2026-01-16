package com.reptilemanagement.mapper;

import com.reptilemanagement.domain.Reptile;
import com.reptilemanagement.dto.ReptileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Reptile entities and DTOs.
 * Uses MapStruct for automatic mapping implementation generation.
 */
@Mapper(componentModel = "spring")
public interface ReptileMapper {
    /**
     * Converts a Reptile entity to a ReptileDto.
     * @param reptile the entity to convert
     * @return the corresponding DTO
     */
    ReptileDto toDto(Reptile reptile);

    /**
     * Converts a ReptileDto to a Reptile entity.
     * @param reptileDto the DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reptile toEntity(ReptileDto reptileDto);
}