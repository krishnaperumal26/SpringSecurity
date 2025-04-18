package com.users.user_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class User extends Base{

    private String email;
    private String password;
    private String name;
    @ManyToMany
    private List<Role> roles;
}


 /*
User --- Roles

1 ------ M
M ------1

 */