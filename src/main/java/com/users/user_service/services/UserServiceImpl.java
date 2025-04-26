package com.users.user_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.user_service.dtos.SendEmailDto;
import com.users.user_service.models.Token;
import com.users.user_service.models.User;
import com.users.user_service.repositories.TokenRepository;
import com.users.user_service.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
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
    KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper;

    UserServiceImpl(UserRepository UserRepository,
                    BCryptPasswordEncoder bCryptPasswordEncoder,
                    TokenRepository tokenRepository,
                    KafkaTemplate<String, String> kafkaTemplate,
                    ObjectMapper objectMapper) {
        this.userRepository = UserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public User signUp(String name, String email, String password) {
       User user = new User();
       user.setName(name);
       user.setEmail(email);
       user.setPassword(bCryptPasswordEncoder.encode(password));

       SendEmailDto sendEmailDto = new SendEmailDto();
       sendEmailDto.setFrom("contact@nanaltech.com");
       sendEmailDto.setTo(email);
       sendEmailDto.setSubject("User Registration");
       sendEmailDto.setBody("Welcome, " + name + ". Your account has been created successfully.");
       String sendEmailDtoString;
        try {
            sendEmailDtoString =objectMapper.writeValueAsString(sendEmailDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send("sendEmail", sendEmailDtoString);
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
