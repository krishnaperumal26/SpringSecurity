package com.users.user_service.dtos;

import com.users.user_service.models.User;
import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String password;

    public static UserDto from(User user) {
        if(user==null) return null;
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
