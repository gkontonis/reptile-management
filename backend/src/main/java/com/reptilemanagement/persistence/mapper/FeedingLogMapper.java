package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.FeedingLog;
import com.reptilemanagement.persistence.dto.FeedingLogDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between FeedingLog entities and DTOs.
 * Extends BaseMapper to inherit common mapping operations.
 */
@Mapper(componentModel = "spring")
public interface FeedingLogMapper extends BaseMapper<FeedingLog, FeedingLogDto> {
    // All standard CRUD mapping methods inherited from BaseMapper
}