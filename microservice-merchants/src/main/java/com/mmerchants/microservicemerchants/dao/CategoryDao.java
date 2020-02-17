package com.mmerchants.microservicemerchants.dao;

import com.mmerchants.microservicemerchants.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    Optional<Category> findFirstByCategoryName(String name);


}
