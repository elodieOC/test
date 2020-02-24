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
     * <p>Lists all categories</p>
     * @return a list
     */
    @GetMapping(value= "/Categories")
    public List<Category> listCategories() {
        List<Category> categories = categoryDao.findAll();
        return categories;
    }

    /**
     * <p>Lists all icons</p>
     * @return a list
     */
    @GetMapping(value= "/Icons")
    public List<CategoryIconDTO> listIcons() {
        List<CategoryIcon> icons = iconDao.findAll();
        List<CategoryIconDTO> dtos = new ArrayList<>();
        for (CategoryIcon ci: icons){
            CategoryIconDTO dto = new CategoryIconDTO();
            dto.setId(ci.getId());
            dto.setIcon(Base64.getEncoder().encodeToString(ci.getIcon()));
            dtos.add(dto);
        }
        return dtos;
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
        CategoryIcon iconAdded = new CategoryIcon();
        icon.setIcon(Base64.getDecoder().decode(catDTO.getIcon()));
        List<CategoryIcon> icons = iconDao.findAll();
        //if icons exist in db check if this one exists
        if (!icons.isEmpty()) {
            int counter = 0;
            for (CategoryIcon ic : icons) {
                //if found, counter increments and sets i to existing icon
                if (Arrays.equals(ic.getIcon(), icon.getIcon())) {
                    //TODO ajouter message dans la page System.out.println("ICON ALREADY EXISTS");
                    throw new CannotAddException("UniqueFail");
                }
            }
            //if it doesn't exist, save it
            if (counter == 0) {
                iconAdded = iconDao.save(icon);
            }
        }
        else {
            iconAdded = iconDao.save(icon);
        }
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
        Integer idCat = catDTO.getId();
        Category originalCat = categoryDao.getById(idCat);
        if(catDTO.getCategoryName()!=null&&!originalCat.getCategoryName().equals(catDTO.getCategoryName())){
            originalCat.setCategoryName(catDTO.getCategoryName());
        }
        //if an image is sent by catDTO
        if(!catDTO.getIcon().isEmpty()) {
            //Create a categoryIcon
            CategoryIcon i = new CategoryIcon();
            i.setIcon(Base64.getDecoder().decode(catDTO.getIcon()));
            //if original category doesn't have an icon OR if icons are different
            if (originalCat.getCategoryIcon() == null || !Arrays.equals(originalCat.getCategoryIcon().getIcon(), i.getIcon())) {
                List<CategoryIcon> icons = iconDao.findAll();
                //if icons exist in db check if this one exists
                if (!icons.isEmpty()) {
                    for (CategoryIcon ic : icons) {
                        //if found, counter increments and throws error
                        if (Arrays.equals(ic.getIcon(), i.getIcon())) {
                            //TODO ajouter message dans la page System.out.println("ICON ALREADY EXISTS");
                            throw new CannotAddException("UniqueFail");
                        }
                    }
                    //if it doesn't exist, save it
                    iconDao.save(i);
                    i=iconDao.findTopByOrderByIdDesc();
                }
                //if icons is empty in db, save it automatically
                else{
                    iconDao.save(i);
                    i=iconDao.findTopByOrderByIdDesc();
                }
                //bind the icon to the category
                originalCat.setCategoryIcon(i);
            }
        }
        System.out.println("ORIG CAT ICON ID "+originalCat.getCategoryIcon().getId());
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
