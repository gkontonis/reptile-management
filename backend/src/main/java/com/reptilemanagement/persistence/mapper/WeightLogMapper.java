package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.WeightLog;
import com.reptilemanagement.persistence.dto.WeightLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between WeightLog entities and DTOs.
 * Uses MapStruct for automatic mapping implementation generation.
 */
@Mapper(componentModel = "spring")
public interface WeightLogMapper {
    /**
     * Converts a WeightLog entity to a WeightLogDto.
     * @param weightLog the entity to convert
     * @return the corresponding DTO
     */
    WeightLogDto toDto(WeightLog weightLog);

    /**
     * Converts a WeightLogDto to a WeightLog entity.
     * @param weightLogDto the DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    WeightLog toEntity(WeightLogDto weightLogDto);
}