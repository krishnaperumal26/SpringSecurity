package com.users.user_service.services;

import com.users.user_service.models.Token;
import com.users.user_service.models.User;
import org.springframework.stereotype.Service;

public interface IUserService {

    User signUp(String name, String email, String password);
    Token login(String email, String password);
    void logout(String token);
    User validateToken(String token);
}
