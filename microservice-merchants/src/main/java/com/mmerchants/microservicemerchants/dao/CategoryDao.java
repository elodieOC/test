package com.mmerchants.microservicemerchants.dao;

import com.mmerchants.microservicemerchants.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {




}
