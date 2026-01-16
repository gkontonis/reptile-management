package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.SheddingLog;
import com.reptilemanagement.persistence.dto.SheddingLogDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between SheddingLog entities and DTOs.
 * Extends BaseMapper to inherit common mapping operations.
 */
@Mapper(componentModel = "spring")
public interface SheddingLogMapper extends BaseMapper<SheddingLog, SheddingLogDto> {
    // All standard CRUD mapping methods inherited from BaseMapper
}