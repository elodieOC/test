package com.mmerchants.microservicemerchants.dao;

import com.mmerchants.microservicemerchants.model.CategoryIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
public interface CategoryIconDao extends JpaRepository<CategoryIcon, Integer> {

    @Transactional
    CategoryIcon getCategoryIconById(Integer id);
    CategoryIcon findTopByOrderByIdDesc();


}
