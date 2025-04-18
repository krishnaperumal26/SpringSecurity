package com.users.user_service.services;

import com.users.user_service.models.Token;
import com.users.user_service.models.User;
import com.users.user_service.repositories.TokenRepository;
import com.users.user_service.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    TokenRepository tokenRepository;

    UserServiceImpl(UserRepository UserRepository,
                    BCryptPasswordEncoder bCryptPasswordEncoder,
                    TokenRepository tokenRepository
    ) {
        this.userRepository = UserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;

    }

    @Override
    public User singUp(String name, String email, String password) {
       User user = new User();
       user.setName(name);
       user.setEmail(email);
       user.setPassword(bCryptPasswordEncoder.encode(password));
       return userRepository.save(user);
    }

    @Override
    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty())
        {
            //throw exception or redirect to login or both
            return null;
        }
        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword()))
        {
            return null;
        }

        Token token = new Token();
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphabetic(128));
        //UUID.randomUUID().toString(); //alternate way to generate token
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DATE, 30);
        Date expiryDate = calender.getTime();

        token.setExpiryAt(expiryDate);
        //Save Token and return
        return tokenRepository.save(token);
    }

    @Override
    @Transactional
    public void logout(String token) {
        tokenRepository.updateDeletedByValue(token);

    }

    @Override
    public User validateToken(String token) {

        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());
        if(optionalToken.isEmpty())
        {
            return null;
        }

        return optionalToken.get().getUser();
    }
}
