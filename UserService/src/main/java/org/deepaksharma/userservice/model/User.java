package org.deepaksharma.userservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.deepaksharma.userservice.enums.UserIdentificationType;
import org.deepaksharma.userservice.enums.UserStatus;
import org.deepaksharma.userservice.enums.UserType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 30)
    String name;

    @Column(length = 50, unique = true)
    String email;

    @Column(length = 20, unique = true, nullable = false)
    String phoneNo;

    @Column(length = 100, nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    UserType userType;

    @Enumerated(EnumType.STRING)
    UserStatus userStatus;


    String authorities; // ADMIN, USER

    @Enumerated(EnumType.STRING)
    UserIdentificationType identificationType;

    @Column(length = 20, unique = true)
    String identificationValue;

    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;

    @Override
    public String getUsername() {
        return phoneNo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
