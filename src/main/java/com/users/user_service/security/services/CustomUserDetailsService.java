package com.users.user_service.security.services;

import com.users.user_service.exception.UserNotFoundException;
import com.users.user_service.models.User;
import com.users.user_service.repositories.UserRepository;
import com.users.user_service.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService  {
    UserRepository userRepository;

    CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if(optionalUser.isEmpty())
        {
            throw new UsernameNotFoundException("User not found for "+username);
        }
        User user = optionalUser.get();

        return new CustomUserDetails(user);
    }
}
