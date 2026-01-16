package com.reptilemanagement.mapper;

import com.reptilemanagement.domain.SheddingLog;
import com.reptilemanagement.dto.SheddingLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between SheddingLog entities and DTOs.
 * Uses MapStruct for automatic mapping implementation generation.
 */
@Mapper(componentModel = "spring")
public interface SheddingLogMapper {
    /**
     * Converts a SheddingLog entity to a SheddingLogDto.
     * @param sheddingLog the entity to convert
     * @return the corresponding DTO
     */
    SheddingLogDto toDto(SheddingLog sheddingLog);

    /**
     * Converts a SheddingLogDto to a SheddingLog entity.
     * @param sheddingLogDto the DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SheddingLog toEntity(SheddingLogDto sheddingLogDto);
}