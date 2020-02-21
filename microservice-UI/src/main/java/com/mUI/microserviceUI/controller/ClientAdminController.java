package com.mUI.microserviceUI.controller;


import com.mUI.microserviceUI.beans.*;
import com.mUI.microserviceUI.proxies.MicroserviceMerchantsProxy;
import com.mUI.microserviceUI.proxies.MicroserviceUsersProxy;
import com.mUI.microserviceUI.utils.ImageFileProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        for (CategoryBean c:cats){
            if (c.getCategoryIcon() != null) {
                c.setIconDto(merchantsProxy.getCategoryIcon(c.getCategoryIcon()));
            }
        }
        List<MerchantBean> merchants = merchantsProxy.listMerchants();
        model.addAttribute("users", users);
        model.addAttribute("shops", merchants);
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
        AddCategoryDTO categoryDTO = new AddCategoryDTO();
        boolean format = ImageFileProcessing.checkIfImageIsRightFormat(imageFile);
        boolean size = ImageFileProcessing.checkIfImageSizeOk(imageFile);
        if(format && size){
            categoryDTO.setCategoryName(categoryName);
            byte[] tab = ImageFileProcessing.getImageForEntityAddFromForm(imageFile);
            categoryDTO.setIcon(Base64.getEncoder().encodeToString(tab));
            try {
                merchantsProxy.addCategory(categoryDTO);
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
        editCategoryDTO.setIcon(categoryBean.getIconDto().getIcon());
        model.addAttribute("cat", editCategoryDTO);
        return "edit-category";
    }

    /**
     * edits a shop category
     * @param model
     * @return form page
     */
    @RequestMapping(value = "/Admin/Categories/edit")
    public String editCat(@ModelAttribute("cat") EditCategoryDTO editCategoryDTO, Model model){
        CategoryBean cat = merchantsProxy.showCategory(editCategoryDTO.getId());
        if(!editCategoryDTO.getCategoryName().isEmpty() && !cat.getCategoryName().equals(editCategoryDTO.getCategoryName())){
            cat.setCategoryName(editCategoryDTO.getCategoryName());
        }
        if (!editCategoryDTO.getImageFile().isEmpty()){
            byte[] tab = ImageFileProcessing.getImageForEntityAddFromForm(editCategoryDTO.getImageFile());
            CategoryIconBean categoryIconBean = new CategoryIconBean();
            categoryIconBean.setIcon(Base64.getEncoder().encodeToString(tab));
            cat.setIconDto(categoryIconBean);
        }
        try{
            merchantsProxy.editCategory(cat);
            return "redirect:/Admin";
        }catch (Exception e){
            e.printStackTrace();
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