package com.reptilemanagement.persistence.mapper;

import com.reptilemanagement.persistence.domain.User;
import com.reptilemanagement.persistence.dto.CreateUserRequest;
import com.reptilemanagement.persistence.dto.UserDetailDto;
import com.reptilemanagement.persistence.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for User entity and its DTOs.
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
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUserRequest request);
}
