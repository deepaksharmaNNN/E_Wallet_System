package org.deepaksharma.userservice.mapper;

import lombok.experimental.UtilityClass;
import org.deepaksharma.userservice.dto.CreateUserRequest;
import org.deepaksharma.userservice.enums.UserStatus;
import org.deepaksharma.userservice.model.User;

@UtilityClass
public class UserMapper {
    public User mapToUser(CreateUserRequest createUserRequest) {
        return User.builder()
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .phoneNo(createUserRequest.getPhoneNo())
                .identificationType(createUserRequest.getIdentificationType())
                .identificationValue(createUserRequest.getIdentificationValue())
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}
