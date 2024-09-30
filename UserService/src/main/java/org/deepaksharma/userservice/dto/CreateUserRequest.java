package org.deepaksharma.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.deepaksharma.userservice.enums.UserIdentificationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {

    @NotBlank
    String name;

    @NotBlank
    String email;

    @NotBlank
    String password;

    @NotBlank
    String phoneNo;

    @NotNull
    UserIdentificationType identificationType;

    @NotBlank
    String identificationValue;
}
