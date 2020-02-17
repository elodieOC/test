package com.mUI.microserviceUI.controller;


import com.mUI.microserviceUI.beans.CategoryBean;
import com.mUI.microserviceUI.beans.EditCategoryDTO;
import com.mUI.microserviceUI.beans.UserBean;
import com.mUI.microserviceUI.proxies.MicroserviceMerchantsProxy;
import com.mUI.microserviceUI.proxies.MicroserviceUsersProxy;
import com.mUI.microserviceUI.utils.ImageFileProcessing;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Base64;
import java.util.List;

/**
 * <h2>Controller linking with microservice-users</h2>
 */
@Controller
public class ClientAdminController {

    @Autowired
    private MicroserviceUsersProxy usersProxy;
    @Autowired
    private MicroserviceMerchantsProxy merchantsProxy;

    /**
     * <p>Lists all users</p>
     * @param model
     * @return admin-infos.html
     */
    @GetMapping("/Admin")
    public String adminListUsers(Model model){
        List<UserBean> users =  usersProxy.listUsers();
        List<CategoryBean> cats =  merchantsProxy.listCategories();
        model.addAttribute("users", users);
        model.addAttribute("cats", cats);
        return "admin-infos";
    }

    /**
     * Form page to add a new shop category
     * @param model
     * @return form page
     */
    @GetMapping("/Admin/Categories/nouvelle-categorie")
    public String addCategoryForm(Model model){
        return "add-category";
    }

    /**
     * Adds a new shop category
     * @param model
     * @return form page
     */
    @RequestMapping(value = "/Admin/Categories/add-cat",  consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    public String addCat(@RequestParam("categoryName") String categoryName, @RequestPart("file") MultipartFile imageFile, Model model){
        CategoryBean categoryBean = new CategoryBean();
        boolean format = ImageFileProcessing.checkIfImageIsRightFormat(imageFile);
        boolean size = ImageFileProcessing.checkIfImageSizeOk(imageFile);
        if(format && size){
            categoryBean.setCategoryName(categoryName);
            byte[] tab = ImageFileProcessing.getImageForEntityAddFromForm(imageFile);
            categoryBean.setIcon(Base64.getEncoder().encodeToString(tab));
            try {
                merchantsProxy.addCategory(categoryBean);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "redirect:/Admin";
    }

    /**
     * Form page to edit a shop category
     * @param model
     * @return form page
     */
    @GetMapping("/Admin/Categories/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model){
        CategoryBean categoryBean = merchantsProxy.showCategory(id);
        EditCategoryDTO editCategoryDTO = new EditCategoryDTO();
        editCategoryDTO.setId(id);
        editCategoryDTO.setCategoryName(categoryBean.getCategoryName());
        editCategoryDTO.setIcon(categoryBean.getIcon());
        model.addAttribute("cat", editCategoryDTO);
        return "edit-category";
    }

    /**
     * edits a shop category
     * @param model
     * @return form page
     */
    @RequestMapping(value = "/Admin/Categories/edit")
    public String editCat(@ModelAttribute("cat")EditCategoryDTO editCategoryDTO, @RequestParam(value = "imageFile", required = false) MultipartFile file, Model model){
        CategoryBean cat = merchantsProxy.showCategory(editCategoryDTO.getId());
        if(!cat.getCategoryName().equals(editCategoryDTO.getCategoryName())){
            cat.setCategoryName(editCategoryDTO.getCategoryName());
        }
        if (!file.isEmpty()){
            byte[] tab = ImageFileProcessing.getImageForEntityAddFromForm(file);
            cat.setIcon(Base64.getEncoder().encodeToString(tab));
        }
        try{
            merchantsProxy.editCategory(cat);
            return "admin-infos";
        }catch (Exception e){
            model.addAttribute("errorMessage", e);
            return "edit-category";
        }
    }

    @RequestMapping("/Admin/Categories/delete/{id}")
    public String deleteCat(@PathVariable("id") Integer id){
        merchantsProxy.deleteCategory(id);
        return "redirect:/Admin";
    }
}