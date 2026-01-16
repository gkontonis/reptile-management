package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.Reptile;
import com.reptilemanagement.persistence.dto.ReptileDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Reptile entities and DTOs.
 * Extends BaseMapper to inherit common mapping operations.
 */
@Mapper(componentModel = "spring")
public interface ReptileMapper extends BaseMapper<Reptile, ReptileDto> {
    // All standard CRUD mapping methods inherited from BaseMapper
}