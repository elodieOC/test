package com.mmerchants.microservicemerchants.controller;

import com.mmerchants.microservicemerchants.dao.CategoryDao;
import com.mmerchants.microservicemerchants.dao.CategoryIconDao;
import com.mmerchants.microservicemerchants.exceptions.CannotAddException;
import com.mmerchants.microservicemerchants.exceptions.NotFoundException;
import com.mmerchants.microservicemerchants.model.Category;
import com.mmerchants.microservicemerchants.model.CategoryDTO;
import com.mmerchants.microservicemerchants.model.CategoryIcon;
import com.mmerchants.microservicemerchants.model.CategoryIconDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>Lists all categories</p>
     * @return a list
     */
    @GetMapping(value= "/Categories")
    public List<Category> listCategories() {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        List<Category> categories = categoryDao.findAll();
        log.info("Listing of all catégories");
        return categories;
    }

    /**
     * <p>Lists all icons</p>
     * @return a list of icon dtos (byte[] to String)
     */
    @GetMapping(value= "/Icons")
    public List<CategoryIconDTO> listIcons() {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        List<CategoryIcon> icons = iconDao.findAll();
        log.info("Listing of all icons");
        List<CategoryIconDTO> dtos = new ArrayList<>();
        for (CategoryIcon ci: icons){
            CategoryIconDTO dto = new CategoryIconDTO();
            dto.setId(ci.getId());
            dto.setIcon(Base64.getEncoder().encodeToString(ci.getIcon()));
            dtos.add(dto);
        }
        log.info("Returning list of all icons with images as Strings");
        return dtos;
    }

    /**
     * <p>Adds a new category to db</p>
     * @param catDTO
     * @return responseEntity
     */
    @PostMapping(value = "/Categories/add-category")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO catDTO) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        if(categoryDao.findFirstByCategoryName(catDTO.getCategoryName()).isPresent()){
            log.error("failure add category: name already exists");
            throw new CannotAddException("UniqueFail");
        }
        Category category = new Category();
        CategoryIcon icon = new CategoryIcon();
        CategoryIcon iconAdded = new CategoryIcon();
        icon.setIcon(Base64.getDecoder().decode(catDTO.getIcon()));
        List<CategoryIcon> icons = iconDao.findAll();
        log.info("Listing of all icons");
        //if icons exist in db check if this one exists
        if (!icons.isEmpty()) {
            int counter = 0;
            for (CategoryIcon ic : icons) {
                //if found, counter increments and sets i to existing icon
                if (Arrays.equals(ic.getIcon(), icon.getIcon())) {
                    log.error("failure add category: image already exists");
                    throw new CannotAddException("UniqueFail");
                }
            }
            //if it doesn't exist, save it
            if (counter == 0) {
                iconAdded = iconDao.save(icon);
            }
        }
        //if no icon exist in db save it
        else {
            iconAdded = iconDao.save(icon);
        }
        if (iconAdded == null) {
            log.error("failure add category");
            throw new CannotAddException("AddFail");}
        category.setCategoryName(catDTO.getCategoryName());
        category.setCategoryIcon(iconAdded);
        Category categoryAdded=  categoryDao.save(category);
        log.info("add category");
        if (categoryAdded == null) {
            log.error("failure add category");
            throw new CannotAddException("AddFail");
        }
        log.info("category added");
        return new ResponseEntity<>(categoryAdded, HttpStatus.CREATED);
    }

    /**
     * Edits category information if they've been changed
     * @param catDTO
     * @return user
     */
    @PostMapping(value = "/Categories/edit")
    ResponseEntity<Category> editCategory(@RequestBody CategoryDTO catDTO)  {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Integer idCat = catDTO.getId();
        Category originalCat = categoryDao.getById(idCat);
        log.info("search by id for category to be modified");
        if(catDTO.getCategoryName()!=null&&!originalCat.getCategoryName().equals(catDTO.getCategoryName())){
            originalCat.setCategoryName(catDTO.getCategoryName());
            log.info("changing name of category to be modified");
        }
        //if an image is sent by catDTO
        if(catDTO.getIcon()!=null) {
            log.info("an image has been sent through edit form.");
            //Create a categoryIcon
            CategoryIcon i = new CategoryIcon();
            i.setIcon(Base64.getDecoder().decode(catDTO.getIcon()));
            //if original category doesn't have an icon OR if icons are different
            if (originalCat.getCategoryIcon() == null || !Arrays.equals(originalCat.getCategoryIcon().getIcon(), i.getIcon())) {
                log.info("category to modify has no icon or the icon is different from the form one");
                List<CategoryIcon> icons = iconDao.findAll();
                log.info("Listing of all icons");
                //if icons exist in db check if this one exists
                if (!icons.isEmpty()) {
                    log.info("icons table is not empty");
                    for (CategoryIcon ic : icons) {
                        //if found, counter increments and throws error
                        if (Arrays.equals(ic.getIcon(), i.getIcon())) {
                            log.error("Failure edit category: icon exists in db");
                            throw new CannotAddException("UniqueFail");
                        }
                    }
                    log.info("icon doesn't exist in db");
                    //if it doesn't exist, save it
                    iconDao.save(i);
                    log.info("saving icon");
                    i=iconDao.findTopByOrderByIdDesc();
                }
                //if icons is empty in db, save it automatically
                else{
                    log.info("icons table is empty");
                    iconDao.save(i);
                    log.info("saving icon");
                    i=iconDao.findTopByOrderByIdDesc();
                }
                //bind the icon to the category
                originalCat.setCategoryIcon(i);
                log.info("setting given icon to category to be modified");
            }
        }
        categoryDao.save(originalCat);
        log.info("Category edited");
        return new ResponseEntity<>(originalCat, HttpStatus.OK);
    }

    /**
     * <p>shows a category by its id</p>
     * @param id
     * @return the category
     */
    @GetMapping(value = "/Categories/{id}")
    public CategoryDTO showCategory(@PathVariable Integer id) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalCategory(id);
        Category category = categoryDao.getById(id);
        CategoryDTO dto = new CategoryDTO();
        log.info("building category dto to send image through API");
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        if(category.getCategoryIcon()!=null) {
            log.info("category has icon: set image to String");
            dto.setIcon(Base64.getEncoder().encodeToString(category.getCategoryIcon().getIcon()));
        }
        else {
            log.info("category has no icon: set image to empty String");
            dto.setIcon("");
        }
        log.info("Returning category dto (image as String)");
        return dto;
    }
    /**
     * <p>gets a category by its id without icons details (only its id)</p>
     * @param id
     * @return the category
     */
    @GetMapping(value = "/Category/{id}")
    public Category getCategory(@PathVariable Integer id) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalCategory(id);
        Category category = categoryDao.getById(id);
        log.info("returning category");
        return category;
    }

    /**
     * <p>gets a categoryIcon by its id </p>
     * @param id
     * @return the categoryIcon
     */
    @GetMapping(value = "/CategoryIcon/{id}")
    public CategoryIconDTO getCategoryIcon(@PathVariable Integer id) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<CategoryIcon> categoryIcon = iconDao.findById(id);
        log.info("searching Optional<CategoryIcon> by id");
        if(!categoryIcon.isPresent()) {
            log.error("Failure: categoryIcon id " + id + " doesn't exist.");
            throw new NotFoundException("L'icon avec l'id " + id + " est INTROUVABLE.");
        }
        CategoryIcon catIcon = iconDao.getCategoryIconById(id);
        CategoryIconDTO dto = new CategoryIconDTO();
        log.info("building categoryIcon dto to send image through API");
        dto.setId(catIcon.getId());
        dto.setIcon(Base64.getEncoder().encodeToString(catIcon.getIcon()));
        log.info("Returning categoryIcon dto (image as String)");
        return dto;
    }

    /**
     * <p>deletes a merchant from db and all its datas</p>
     * @param id
     */
    @PostMapping(value = "/Categories/delete/{id}")
    public void deleteCategory(@PathVariable Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalCategory(id);
        Category catToDelete = categoryDao.getOne(id);
        categoryDao.delete(catToDelete);
        log.info("category deleted");
    }

    /**
     * Searches for optional Category.
     * @param id
     */
    private void searchOptionalCategory(Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<Category> cat = categoryDao.findById(id);
        log.info("searching Optional<Category> by id");
        if(!cat.isPresent()) {
            log.error("Failure: category id " + id + " doesn't exist.");
            throw new NotFoundException("La categorie avec l'id " + id + " est INTROUVABLE.");
        }
    }
}
