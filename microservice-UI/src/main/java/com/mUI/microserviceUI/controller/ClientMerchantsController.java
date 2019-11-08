package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.MerchantBean;
import com.mUI.microserviceUI.exceptions.CannotAddException;
import com.mUI.microserviceUI.proxies.MicroserviceMailingProxy;
import com.mUI.microserviceUI.proxies.MicroserviceMerchantsProxy;
import com.mUI.microserviceUI.proxies.MicroserviceUsersProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <h2>Controller linking with microservice-merchants</h2>
 */
@Controller
public class ClientMerchantsController {

    @Autowired
    private MicroserviceMerchantsProxy merchantsProxy;
    @Autowired
    private MicroserviceUsersProxy usersProxy;
    @Autowired
    private MicroserviceMailingProxy mailingProxy;


    /*
    ***************************************
    * Merchant List
    * *************************************
     */

    /**
     * <p>show list of possible merchants</p>
     * @param model
     * @return merchants.html
     */
    @GetMapping("/Marchands")
    public String listMerchants(Model model){
        List<MerchantBean> merchantBeanList = merchantsProxy.listMerchants();
        model.addAttribute("merchants", merchantBeanList);
        return "merchants";
    }
    /*
     **************************************
     * Merchant register
     * ************************************
     */


    /**
     * <p>Page that displays a form to register a merchant</p>
     * @param model attribute passed to jsp page
     * @return login page
     */
    @GetMapping("/Marchands/nouvelle-boutique")
    public String registerPage(Model model) {
        MerchantBean merchantBean = new MerchantBean();
        model.addAttribute("merchant", merchantBean);
        return "register-shop";
    }

    /**
     * <p>Process called after the submit button is clicked on register page</p>
     * @param theMerchant merchant being created
     * @param request servlet request
     * @param model attribute passed to jsp page
     * @return page to show depending on result of process
     */
    @PostMapping("/Marchands/add-shop")
    public String saveMerchant(@ModelAttribute("merchant") MerchantBean theMerchant, HttpServletRequest request, ModelMap model) {
        String toBeReturned;
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        theMerchant.setUserId(userId);
        try {
            MerchantBean merchantToRegister = merchantsProxy.addMerchant(theMerchant);
            toBeReturned = "redirect:/Utilisateurs/MonProfil/"+userId;
        }
        catch (Exception e){
            e.printStackTrace();
            if(e instanceof CannotAddException){
                model.addAttribute("errorMessage", e.getMessage());
            }
            toBeReturned = "register-shop";
        }
        return toBeReturned;
    }

    /**
     * shows details of particular merchant with its id
     * @param shopId
     * @param model
     * @return merchant-details.html
     */
    @RequestMapping("/Marchands/{shopId}")
    public String merchantDetails(@PathVariable Integer shopId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        MerchantBean merchant = merchantsProxy.showMerchant(shopId);
        model.addAttribute("merchant", merchant);
        model.addAttribute("session", session);
        return "merchant-profile";
    }


    /*
     **************************************
     * Merchant delete
     * ************************************
     */

    /**
     * <p>deletes a merchant when merchant clicks on suppress button</p>
     * @param id
     * @return url depending on function result
     */
    @RequestMapping(value = "/Marchands/delete/{id}", method = RequestMethod.POST)
    public String deleteMerchant(@PathVariable("id") Integer id, HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        merchantsProxy.deleteMerchant(id);
        return "redirect:/Utilisateurs/MonProfil/"+userId;
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex){
        return new ModelAndView("redirect:/Accueil");
    }

}