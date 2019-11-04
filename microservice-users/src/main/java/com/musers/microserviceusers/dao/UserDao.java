package com.musers.microserviceusers.dao;


import com.musers.microserviceusers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Optional<User> findFirstByEmail(String email);
    Optional<User> findByResetToken(String resetToken);


}
