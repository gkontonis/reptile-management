package com.reptilemanagement.mapper;

import com.reptilemanagement.domain.ReptileImage;
import com.reptilemanagement.dto.ReptileImageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between ReptileImage entities and DTOs.
 * Uses MapStruct for automatic mapping implementation generation.
 */
@Mapper(componentModel = "spring")
public interface ReptileImageMapper {
    /**
     * Converts a ReptileImage entity to a ReptileImageDto.
     * Excludes the binary imageData field.
     * 
     * @param reptileImage the entity to convert
     * @return the corresponding DTO
     */
    @Mapping(target = "id", source = "id")
    ReptileImageDto toDto(ReptileImage reptileImage);

    /**
     * Converts a ReptileImageDto to a ReptileImage entity.
     * Note: imageData must be set separately as it's not in the DTO.
     * 
     * @param reptileImageDto the DTO to convert
     * @return the corresponding entity
     */
    @Mapping(target = "imageData", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    ReptileImage toEntity(ReptileImageDto reptileImageDto);
}
