package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.FeedingLog;
import com.reptilemanagement.persistence.dto.FeedingLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between FeedingLog entities and DTOs.
 * Uses MapStruct for automatic mapping implementation generation.
 */
@Mapper(componentModel = "spring")
public interface FeedingLogMapper {
    /**
     * Converts a FeedingLog entity to a FeedingLogDto.
     * @param feedingLog the entity to convert
     * @return the corresponding DTO
     */
    FeedingLogDto toDto(FeedingLog feedingLog);

    /**
     * Converts a FeedingLogDto to a FeedingLog entity.
     * @param feedingLogDto the DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    FeedingLog toEntity(FeedingLogDto feedingLogDto);
}