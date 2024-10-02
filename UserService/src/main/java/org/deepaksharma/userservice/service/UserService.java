package org.deepaksharma.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deepaksharma.userservice.dto.CreateUserRequest;
import org.deepaksharma.userservice.enums.UserType;
import org.deepaksharma.userservice.mapper.UserMapper;
import org.deepaksharma.userservice.model.User;
import org.deepaksharma.userservice.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.deepaksharma.userservice.constants.KafkaConstants.USER_CREATION_TOPIC;
import static org.deepaksharma.userservice.constants.UserCreationTopicConstants.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setUserType(UserType.USER);
        user.setAuthorities("USER");
        log.info("Creating user: {}", user);
        userRepository.save(user);
        log.info("User created: {}", user);

        //publish to kafka
        // notification service -> username, email
        // wallet service -> username, phoneNo, userStatus

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(EMAIL, user.getEmail());
        objectNode.put(PHONE_NO, user.getPhoneNo());
        objectNode.put(USER_ID, user.getId());
        objectNode.put(NAME, user.getName());


        kafkaTemplate.send(USER_CREATION_TOPIC, objectNode.toString());
        log.info("User creation event published to kafka: {}", objectNode);
        return user;
    }
}
