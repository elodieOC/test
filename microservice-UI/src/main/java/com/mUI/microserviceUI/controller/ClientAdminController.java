package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.*;
import com.mUI.microserviceUI.beansDTO.AddCategoryDTO;
import com.mUI.microserviceUI.beansDTO.CategoryDTO;
import com.mUI.microserviceUI.beansDTO.EditCategoryDTO;
import com.mUI.microserviceUI.proxies.MicroserviceMerchantsProxy;
import com.mUI.microserviceUI.proxies.MicroserviceUsersProxy;
import com.mUI.microserviceUI.utils.ImageFileProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>Lists all users</p>
     * @param model
     * @return admin-infos.html
     */
    @GetMapping("/Admin")
    public String adminListUsers(Model model){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("call userProxy");
        List<UserBean> users =  usersProxy.listUsers();
        log.info("call merchantProxy");
        List<CategoryBean> cats =  merchantsProxy.listCategories();
        for (CategoryBean c:cats){
            if (c.getCategoryIcon() != null) {
                c.setIcon(merchantsProxy.getCategoryIcon(c.getCategoryIcon()));
            }
        }
        log.info("call merchantProxy");
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        return "add-category";
    }

    /**
     * Adds a new shop category
     * @param model
     * @return form page
     */
    @RequestMapping(value = "/Admin/Categories/add-cat",  consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    public String addCat(@RequestParam("categoryName") String categoryName, @RequestPart("file") MultipartFile imageFile, Model model){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        AddCategoryDTO categoryDTO = new AddCategoryDTO();
        log.info("check image format");
        boolean format = ImageFileProcessing.checkIfImageIsRightFormat(imageFile);
        log.info("check image size");
        boolean size = ImageFileProcessing.checkIfImageSizeOk(imageFile);
        if(format && size){
            log.info("building categoryDTO to send through API");
            categoryDTO.setCategoryName(categoryName);
            byte[] tab = ImageFileProcessing.getImageForEntityAddFromForm(imageFile);
            categoryDTO.setIcon(Base64.getEncoder().encodeToString(tab));
            try {
                log.info("call merchantProxy");
                merchantsProxy.addCategory(categoryDTO);
            }catch (Exception e){
                log.error("Failure merchantsProxy.addCategory");
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("call merchantProxy");
        List<CategoryBean> cats =  merchantsProxy.listCategories();
        CategoryBean cat = new CategoryBean();
        for (CategoryBean c:cats){
            if (c.getCategoryIcon() != null && c.getId() == id) {
                log.info("call merchantProxy");
                c.setIcon(merchantsProxy.getCategoryIcon(c.getCategoryIcon()));
                cat = c;
            }
        }
        log.info("call merchantProxy");
        CategoryIconBean iconBean = merchantsProxy.getCategoryIcon(cat.getCategoryIcon());
        log.info("building categoryDTO to send through API for editing");
        EditCategoryDTO editCategoryDTO = new EditCategoryDTO();
        editCategoryDTO.setId(id);
        editCategoryDTO.setCategoryName(cat.getCategoryName());
        editCategoryDTO.setIcon(iconBean.getIcon());
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("call merchantProxy");
        List<CategoryBean> cats =  merchantsProxy.listCategories();
        log.info("call merchantProxy");
        CategoryBean cat = merchantsProxy.getCategory(editCategoryDTO.getId());
        log.info("building categoryDTO to send through API for editing");
        CategoryDTO dto = new CategoryDTO();
        dto.setId(editCategoryDTO.getId());
        for (CategoryBean c:cats){
            if (c.getCategoryIcon() != null && c.getId() == editCategoryDTO.getId()) {
                log.info("call merchantProxy");
                c.setIcon(merchantsProxy.getCategoryIcon(c.getCategoryIcon()));
                cat = c;
            }
        }
        if(!editCategoryDTO.getCategoryName().isEmpty() && !cat.getCategoryName().equals(editCategoryDTO.getCategoryName())){
            dto.setCategoryName(editCategoryDTO.getCategoryName());
        }
        if (!editCategoryDTO.getImageFile().isEmpty()){
            byte[] tab = ImageFileProcessing.getImageForEntityAddFromForm(editCategoryDTO.getImageFile());
            dto.setIcon(Base64.getEncoder().encodeToString(tab));
        }
        try{
            log.info("call merchantProxy");
            merchantsProxy.editCategory(dto);
            return "redirect:/Admin";
        }catch (Exception e){
            log.error("Failure merchantsProxy.editCat");
            e.printStackTrace();
            model.addAttribute("errorMessage", e);
            return "edit-category";
        }
    }

    @RequestMapping("/Admin/Categories/delete/{id}")
    public String deleteCat(@PathVariable("id") Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("call merchantProxy");
        merchantsProxy.deleteCategory(id);
        return "redirect:/Admin";
    }
}