package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.WeightLog;
import com.reptilemanagement.persistence.dto.WeightLogDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between WeightLog entities and DTOs.
 * Extends BaseMapper to inherit common mapping operations.
 */
@Mapper(componentModel = "spring")
public interface WeightLogMapper extends BaseMapper<WeightLog, WeightLogDto> {
    // All standard CRUD mapping methods inherited from BaseMapper
}