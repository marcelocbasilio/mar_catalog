package com.marcelocbasilio.catalog.dtos;

import com.marcelocbasilio.catalog.entities.Role;

import java.io.Serializable;
import java.util.Objects;

public class RoleDto implements Serializable {

    private Long id;
    private String authority;

    public RoleDto() {}

    public RoleDto(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDto(Role role) {
        id = role.getId();
        authority = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleDto roleDto = (RoleDto) o;
        return Objects.equals(id, roleDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
