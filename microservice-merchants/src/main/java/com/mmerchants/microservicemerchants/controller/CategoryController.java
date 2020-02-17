package com.mmerchants.microservicemerchants.controller;

import com.mmerchants.microservicemerchants.dao.CategoryDao;
import com.mmerchants.microservicemerchants.exceptions.NotFoundException;
import com.mmerchants.microservicemerchants.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <h2>Controller for Model Category</h2>
 */
@RestController
public class CategoryController {
    @Autowired
    private CategoryDao merchantDao;
    @Autowired
    private CategoryDao categoryDao;

    /**
     * <p>Lists all merchants</p>
     * @return a list
     */
    @GetMapping(value= "/Categories")
    public List<Category> listCategories() {
        return categoryDao.findAll();
    }

     /**
     * <p>shows a category by its id</p>
     * @param id
     * @return the category
     */
    @GetMapping(value = "/Categories/{id}")
    public Optional<Category> showCategory(@PathVariable Integer id) {
        Optional<Category> cat = categoryDao.findById(id);
        if(!cat.isPresent()) {
            throw new NotFoundException("La categorie avec l'id " + id + " est INTROUVABLE.");
        }
        return cat;
    }

      /**
     * <p>deletes a merchant from db and all its datas</p>
     * @param id
     */
    @PostMapping(value = "/Categories/delete/{id}")
    public void deleteCategory(@PathVariable Integer id){
        Optional<Category> cat = categoryDao.findById(id);
        if(!cat.isPresent()) {
            throw new NotFoundException("La categorie avec l'id " + id + " est INTROUVABLE.");
        }
        Category catToDelete = categoryDao.getOne(id);
        categoryDao.delete(catToDelete);
    }

   
}
