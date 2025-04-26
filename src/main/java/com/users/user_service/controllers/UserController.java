package com.users.user_service.controllers;

import com.users.user_service.dtos.*;
import com.users.user_service.models.User;
import com.users.user_service.models.Token;
import com.users.user_service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    IUserService userService;
    UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto)
    {
        User user = userService.signUp(requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword());
        return UserDto.from(user);
    }
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto)
    {
        Token token = userService.login(requestDto.getEmail(),
                requestDto.getPassword());

        return LoginResponseDto.from(token);

    }
    @PostMapping("/logoutt")
    public ResponseEntity<Void> logout(@RequestBody LogOutRequestDto requestDto)
    {
        userService.logout(requestDto.getTokenValidate());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/validate/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable("token") String token)
    {
        User user = userService.validateToken(token);
        if(user == null)
        {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
