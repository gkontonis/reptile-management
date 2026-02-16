package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.PoopLog;
import com.reptilemanagement.persistence.dto.PoopLogDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between PoopLog entities and DTOs.
 * Extends BaseMapper to inherit common mapping operations.
 */
@Mapper(componentModel = "spring")
public interface PoopLogMapper extends BaseMapper<PoopLog, PoopLogDto> {
    // All standard CRUD mapping methods inherited from BaseMapper
}
