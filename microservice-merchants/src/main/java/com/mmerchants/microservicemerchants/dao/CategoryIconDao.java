package com.mmerchants.microservicemerchants.dao;

import com.mmerchants.microservicemerchants.model.Category;
import com.mmerchants.microservicemerchants.model.CategoryIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryIconDao extends JpaRepository<CategoryIcon, Integer> {

    CategoryIcon getCategoryIconById(Integer id);


}
