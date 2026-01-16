package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto<Long> implements Updatable<UserDto> {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void update(UserDto dto) {
        if (dto == null) {
            return;
        }
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.roles = dto.getRoles();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}