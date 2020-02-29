package com.musers.microserviceusers.dao;


import com.musers.microserviceusers.model.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NewsletterDao extends JpaRepository<Newsletter, Integer> {
    Newsletter findByEmail(String email);

    Optional<Newsletter> findFirstByEmail(String email);


}
