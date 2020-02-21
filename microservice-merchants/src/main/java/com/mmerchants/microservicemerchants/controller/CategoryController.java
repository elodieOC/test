package com.mmerchants.microservicemerchants.controller;

import com.mmerchants.microservicemerchants.dao.CategoryDao;
import com.mmerchants.microservicemerchants.dao.CategoryIconDao;
import com.mmerchants.microservicemerchants.exceptions.CannotAddException;
import com.mmerchants.microservicemerchants.exceptions.NotFoundException;
import com.mmerchants.microservicemerchants.model.Category;
import com.mmerchants.microservicemerchants.model.CategoryDTO;
import com.mmerchants.microservicemerchants.model.CategoryIcon;
import com.mmerchants.microservicemerchants.model.CategoryIconDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <h2>Controller for Model Category</h2>
 */
@RestController
public class CategoryController {
    @Autowired
    private CategoryDao merchantDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryIconDao iconDao;

    /**
     * <p>Lists all merchants</p>
     * @return a list
     */
    @GetMapping(value= "/Categories")
    public List<Category> listCategories() {
        List<Category> categories = categoryDao.findAll();
        /*List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category c:categories){
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryName(c.getCategoryName());
            dto.setId(c.getId());
            if(c.getCategoryIcon()!=null) {
                dto.setIcon(Base64.getEncoder().encodeToString(c.getCategoryIcon().getIcon()));
            }
            else {dto.setIcon(null);}
            categoryDTOS.add(dto);
        }*/
        return categories;
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
        Category category = new Category();
        CategoryIcon icon = new CategoryIcon();
        icon.setIcon(Base64.getDecoder().decode(catDTO.getIcon()));
        CategoryIcon iconAdded = iconDao.save(icon);
        if (iconAdded == null) {throw new CannotAddException("AddFail");}
        category.setCategoryName(catDTO.getCategoryName());
        category.setCategoryIcon(iconAdded);
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
        Integer id = catDTO.getId();
        Category originalCat= categoryDao.getById(id);
        Category editedCat = new Category();
        editedCat.setCategoryName(catDTO.getCategoryName());
        if(catDTO.getIcon()!=null) {
            CategoryIcon i = new CategoryIcon();
            i.setIcon(Base64.getDecoder().decode(catDTO.getIcon()));
            editedCat.setCategoryIcon(i);
        }
        if(!originalCat.getCategoryName().equals(editedCat.getCategoryName())){
            originalCat.setCategoryName(editedCat.getCategoryName());
        }
        if(originalCat.getCategoryIcon() == null || !Arrays.equals(originalCat.getCategoryIcon().getIcon(), editedCat.getCategoryIcon().getIcon())){
            originalCat.setCategoryIcon(editedCat.getCategoryIcon());
            List<CategoryIcon> icons = iconDao.findAll();
            if (icons.isEmpty()){
                iconDao.save(originalCat.getCategoryIcon());
            }
            else {
                for (CategoryIcon i : icons) {
                    if (!Arrays.equals(i.getIcon(), originalCat.getCategoryIcon().getIcon())) {
                        iconDao.save(originalCat.getCategoryIcon());
                    }
                }
            }
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
    public CategoryDTO showCategory(@PathVariable Integer id) {
        Optional<Category> cat = categoryDao.findById(id);
        if(!cat.isPresent()) {
            throw new NotFoundException("La categorie avec l'id " + id + " est INTROUVABLE.");
        }
        Category category = categoryDao.getById(id);
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        if(category.getCategoryIcon()!=null) {
            dto.setIcon(Base64.getEncoder().encodeToString(category.getCategoryIcon().getIcon()));
        }
        else {dto.setIcon("");}
        return dto;
    }
    /**
     * <p>gets a category by its id without icons details (only its id)</p>
     * @param id
     * @return the category
     */
    @GetMapping(value = "/Category/{id}")
    public Category getCategory(@PathVariable Integer id) {
        Optional<Category> cat = categoryDao.findById(id);
        if(!cat.isPresent()) {
            throw new NotFoundException("La categorie avec l'id " + id + " est INTROUVABLE.");
        }
        Category category = categoryDao.getById(id);
        return category;
    }

    /**
     * <p>gets a categoryIcon by its id </p>
     * @param id
     * @return the categoryIcon
     */
    @GetMapping(value = "/CategoryIcon/{id}")
    public CategoryIconDTO getCategoryIcon(@PathVariable Integer id) {
        Optional<CategoryIcon> categoryIcon = iconDao.findById(id);
        if(!categoryIcon.isPresent()) {
            throw new NotFoundException("La categorie avec l'id " + id + " est INTROUVABLE.");
        }
        CategoryIcon catIcon = iconDao.getCategoryIconById(id);
        CategoryIconDTO dto = new CategoryIconDTO();
        dto.setId(catIcon.getId());
        dto.setIcon(Base64.getEncoder().encodeToString(catIcon.getIcon()));
        return dto;
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
