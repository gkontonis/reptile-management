package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Detailed DTO for user information including metadata.
 * Used for displaying comprehensive user information in admin interfaces.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailDto extends BaseDto<Long> implements Updatable<UserDetailDto> {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private Integer todoCount;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void update(UserDetailDto dto) {
        if (dto == null) {
            return;
        }
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.roles = dto.getRoles();
        this.todoCount = dto.getTodoCount();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}
