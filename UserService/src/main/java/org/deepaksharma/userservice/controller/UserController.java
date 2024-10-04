package org.deepaksharma.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deepaksharma.userservice.dto.CreateUserRequest;
import org.deepaksharma.userservice.model.User;
import org.deepaksharma.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public User createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    @GetMapping("/user")
    public User getUser(@RequestParam String phoneNo) {
        return userService.getUserByPhoneNo(phoneNo);
    }
}
