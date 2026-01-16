package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.ReptileImage;
import com.reptilemanagement.persistence.dto.ReptileImageDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between ReptileImage entities and DTOs.
 * Extends BaseMapper and overrides toEntity to exclude binary imageData field.
 */
@Mapper(componentModel = "spring")
public interface ReptileImageMapper extends BaseMapper<ReptileImage, ReptileImageDto> {
    
    /**
     * Overrides toEntity to exclude the binary imageData field.
     * The imageData must be set separately when handling file uploads.
     */
    @Override
    @Mapping(target = "imageData", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ReptileImage toEntity(ReptileImageDto reptileImageDto);
}
