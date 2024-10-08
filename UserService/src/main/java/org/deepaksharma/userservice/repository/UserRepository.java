package org.deepaksharma.userservice.repository;

import org.deepaksharma.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPhoneNo(String phoneNo);
}
