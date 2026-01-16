package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.EnclosureCleaning;
import com.reptilemanagement.persistence.dto.EnclosureCleaningDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between EnclosureCleaning entities and DTOs.
 * Extends BaseMapper to inherit common mapping operations.
 */
@Mapper(componentModel = "spring")
public interface EnclosureCleaningMapper extends BaseMapper<EnclosureCleaning, EnclosureCleaningDto> {
    // All standard CRUD mapping methods inherited from BaseMapper
}