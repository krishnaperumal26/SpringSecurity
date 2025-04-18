package com.users.user_service.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Role extends Base{
    private String value;
}
