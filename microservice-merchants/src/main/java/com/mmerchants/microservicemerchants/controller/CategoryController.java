package com.mmerchants.microservicemerchants.controller;

import com.mmerchants.microservicemerchants.dao.CategoryDao;
import com.mmerchants.microservicemerchants.exceptions.CannotAddException;
import com.mmerchants.microservicemerchants.exceptions.NotFoundException;
import com.mmerchants.microservicemerchants.model.Category;
import com.mmerchants.microservicemerchants.model.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * <p>Adds a new category to db</p>
     * @param catDTO
     * @return responseEntity
     */
    @PostMapping(value = "/Categories/add-category")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO catDTO) {
        if(categoryDao.findFirstByCategoryName(catDTO.getCategoryName()).isPresent()){
            throw new CannotAddException("UniqueFail");
        }
        Category category = new Category(catDTO);
        Category categoryAdded=  categoryDao.save(category);
        if (categoryAdded == null) {throw new CannotAddException("AddFail");}
        return new ResponseEntity<Category>(categoryAdded, HttpStatus.CREATED);
    }

    /**
     * Edits category information if they've been changed
     * @param catDTO
     * @return user
     */
    @PostMapping(value = "/Categories/edit")
    ResponseEntity<Category> editCategory(@RequestBody CategoryDTO catDTO)  {
        Category originalCat= categoryDao.getOne(catDTO.getId());
        Category editedCat = new Category(catDTO);
        if(!originalCat.getCategoryName().equals(editedCat.getCategoryName())){
            originalCat.setCategoryName(editedCat.getCategoryName());
        }
        if(!originalCat.getIcon().equals(editedCat.getIcon())){
            originalCat.setIcon(editedCat.getIcon());
        }
        categoryDao.save(originalCat);
        return new ResponseEntity<Category>(originalCat, HttpStatus.OK);
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
