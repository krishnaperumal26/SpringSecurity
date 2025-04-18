package com.users.user_service.dtos;

import com.users.user_service.models.Token;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String tokenValue;

    public static LoginResponseDto from(Token token) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setTokenValue(token.getValue());
        return loginResponseDto;
    }
}
