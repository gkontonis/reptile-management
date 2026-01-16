package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.Enclosure;
import com.reptilemanagement.persistence.dto.EnclosureDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Enclosure entities and DTOs.
 * Extends BaseMapper to inherit common mapping operations.
 */
@Mapper(componentModel = "spring")
public interface EnclosureMapper extends BaseMapper<Enclosure, EnclosureDto> {
    // All standard CRUD mapping methods inherited from BaseMapper
}