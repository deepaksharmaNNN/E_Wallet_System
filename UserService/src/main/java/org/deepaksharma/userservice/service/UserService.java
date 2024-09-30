package org.deepaksharma.userservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deepaksharma.userservice.dto.CreateUserRequest;
import org.deepaksharma.userservice.enums.UserType;
import org.deepaksharma.userservice.mapper.UserMapper;
import org.deepaksharma.userservice.model.User;
import org.deepaksharma.userservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNo(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + username);
        }
        return user;
    }

    public User createUser(@Valid CreateUserRequest createUserRequest) {
        User user = UserMapper.mapToUser(createUserRequest);
        user.setUserType(UserType.USER);
        user.setAuthorities("USER");
        log.info("Creating user: {}", user);
        return userRepository.save(user);
    }
}
