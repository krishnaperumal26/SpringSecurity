package com.users.user_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.util.Date;

@Data
@Entity
public class Token extends Base{
    private String value;
    private Date expiryAt;
    @ManyToOne
    private User user;
}

/*
User --- Token
1  ---- M
1  ----- 1

User : Token = 1:M
Token : User = M:1
 */