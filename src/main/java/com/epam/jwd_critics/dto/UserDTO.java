package com.epam.jwd_critics.dto;

import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;

public class UserDTO {
    private Integer id;
    private Role role;
    private Status status;

    public UserDTO(User user){
        this.id = user.getId();
        this.role = user.getRole();
        this.status = user.getStatus();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
