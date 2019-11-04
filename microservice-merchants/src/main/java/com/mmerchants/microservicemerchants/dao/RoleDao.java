package com.mmerchants.microservicemerchants.dao;


import com.mmerchants.microservicemerchants.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {

}
