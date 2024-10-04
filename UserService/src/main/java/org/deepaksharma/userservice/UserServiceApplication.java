package org.deepaksharma.userservice;

import lombok.RequiredArgsConstructor;
import org.deepaksharma.userservice.enums.UserStatus;
import org.deepaksharma.userservice.enums.UserType;
import org.deepaksharma.userservice.model.User;
import org.deepaksharma.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class UserServiceApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User transactionService = User.builder()
                .phoneNo("transaction-service")
                .password(passwordEncoder.encode("transaction-service"))
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.SERVICE)
                .authorities("SERVICE")
                .build();
        if(userRepository.findByPhoneNo(transactionService.getPhoneNo()) == null) {
            userRepository.save(transactionService);
        }
    }
}
