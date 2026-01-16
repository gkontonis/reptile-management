package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.User;
import com.reptilemanagement.persistence.dto.CreateUserRequest;
import com.reptilemanagement.persistence.dto.UserDetailDto;
import com.reptilemanagement.persistence.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for User entity and its DTOs.
 * <p>
 * Note: User entity does not extend BaseEntity, so this mapper does not extend BaseMapper.
 * However, it still follows similar patterns for audit field handling.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Map User entity to UserDto.
     */
    UserDto toDto(User user);

    /**
     * Map User entity to UserDetailDto.
     */
    @Mapping(target = "todoCount", ignore = true)
    UserDetailDto toDetailDto(User user);

    /**
     * Map CreateUserRequest to User entity.
     * Audit fields and password are ignored as they're set by the service layer.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUserRequest request);
}
