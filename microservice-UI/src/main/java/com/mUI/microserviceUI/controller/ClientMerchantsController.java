package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.MerchantBean;
import com.mUI.microserviceUI.exceptions.BadLoginPasswordException;
import com.mUI.microserviceUI.exceptions.CannotAddException;
import com.mUI.microserviceUI.proxies.MicroserviceMailingProxy;
import com.mUI.microserviceUI.proxies.MicroserviceMerchantsProxy;
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
     **************************************
     * Merchant register
     * ************************************
     */


    /**
     * <p>Page that displays a form to register a merchant</p>
     * @param model attribute passed to jsp page
     * @return login page
     */
    @GetMapping("/Marchands/inscription")
    public String registerPage(Model model) {
        MerchantBean merchantBean = new MerchantBean();
        model.addAttribute("merchant", merchantBean);
        return "register-merchant";
    }

    /**
     * <p>Process called after the submit button is clicked on register page</p>
     * @param theMerchant merchant being created
     * @param request servlet request
     * @param model attribute passed to jsp page
     * @return page to show depending on result of process
     */
    @PostMapping("/Marchands/add-merchant")
    public String saveMerchant(@ModelAttribute("merchant") MerchantBean theMerchant, HttpServletRequest request, ModelMap model) {
        String toBeReturned;
        HttpSession session = request.getSession();
        try {
            MerchantBean merchantToRegister = merchantsProxy.addMerchant(theMerchant);
            toBeReturned = setSessionAttributes(merchantToRegister, session);
        }
        catch (Exception e){
            e.printStackTrace();
            if(e instanceof CannotAddException){
                model.addAttribute("errorMessage", e.getMessage());
            }
            toBeReturned = "register-merchant";
        }
        return toBeReturned;
    }
    /*
     **************************************
     * Merchant login
     * ************************************
     */

    /**
     * <p>Page that displays a form to login a merchant</p>
     * @param model attribute passed to jsp page
     * @return login page
     */
    @GetMapping("/Marchands/connexion")
    public String loginPage(Model model) {
        MerchantBean merchantBean = new MerchantBean();
        model.addAttribute("merchant", merchantBean);
        return "login-merchant";
    }


    @RequestMapping("/Marchands/log-merchant")
    public String logMerchant(@ModelAttribute("merchant") MerchantBean merchantBean, ModelMap model, HttpServletRequest request) {
        String toBeReturned;
        HttpSession session = request.getSession();
        try{
            MerchantBean merchantToConnect = merchantsProxy.logMerchant(merchantBean.getEmail(), merchantBean.getPassword());
            toBeReturned = setSessionAttributes(merchantToConnect, session);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("FIELDS: "+merchantBean.getEmail()+" "+merchantBean.getPassword());
            if(e instanceof BadLoginPasswordException){
                model.addAttribute("errorMessage", "Login ou Mot de Passe incorrect");
            }
            toBeReturned = "login-merchant";
        }
        return toBeReturned;
    }

    /**
     * shows details of particular merchant with its id
     * @param merchantId
     * @param model
     * @return merchant-details.html
     */
    @RequestMapping("/Marchands/MonProfil/{merchantId}")
    public String merchantDetails(@PathVariable Integer merchantId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        MerchantBean merchant = merchantsProxy.showMerchant(merchantId);

        if(!session.getAttribute("loggedInMerchantId").equals(merchantId)){
            System.out.println("Merchant trying to access profile is not the owner of the profile");
            System.out.println("Merchant is: [id:"
                    +session.getAttribute("loggedInMerchantId")+ ", email:"
                    +session.getAttribute("loggedInMerchantEmail")+"]");
            return "redirect:/Accueil";
        }

        model.addAttribute("merchant", merchant);
        model.addAttribute("session", session);
        return "merchant-details";
    }
    /*
     **************************************
     * Merchant logout
     * ************************************
     */
    /**
     * Process called after the logout button is clicked in navbar
     * @param session session
     * @return homepage
     */
    @RequestMapping(value = "/Marchands/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/Accueil";
    }

  /*
     **************************************
     * Merchant reset password
     * ************************************
     */

    /**
     *<p>displays form to send an email to retrieve a link to reset password</p>
     * @param model merchant
     * @return page with form
     */
    @GetMapping(value = "/Marchands/MotDePasseOublie")
    public String forgotPassword(Model model) {
        MerchantBean merchantBean = new MerchantBean();
        model.addAttribute("merchant", merchantBean);
        return "password-forgot";
    }

    /**
     *<p>process called when form to send reset link is validated</p>
     * @param merchantBean merchant
     * @param theModel form model
     * @param request servlet request
     * @return page form
     */
    @RequestMapping("/Marchands/forgot-password")
    public String findMerchantSendLinkForPassword(@ModelAttribute("merchant") MerchantBean merchantBean, ModelMap theModel, HttpServletRequest request) {
        MerchantBean merchantToFind = new MerchantBean();
        String appUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
        try{
            merchantToFind = merchantsProxy.findMerchantForPassword(merchantBean.getEmail());
            theModel.addAttribute("successMessage", "Un email a été envoyé à l'adresse indiquée");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("EMAIL INPUT = "+merchantBean.getEmail());
            theModel.addAttribute("errorMessage", "email inconnu");
        }
        //TODO le mail est bien envoyé mais renvoi le catch: FeignException: status 504 reading MicroserviceMailingProxy#sendLinkForPassword(String,String,String)
        try{
            mailingProxy.sendLinkForPassword(merchantToFind.getEmail(), merchantToFind.getResetToken(), appUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "password-forgot";
    }

    /**
     *<p>displays form to reset password</p>
     * @param model model form
     * @return page form
     */
  @RequestMapping(value = "/Marchands/MotDePasseReset", method = RequestMethod.GET)
    public String resetPasswordForm(Model model, @RequestParam("token") String token) {
      String redirectPage = "";
      try {
          MerchantBean merchantBean = merchantsProxy.findMerchantByToken(token);
          model.addAttribute("merchant", merchantBean);
          model.addAttribute("resetToken", token);
          redirectPage = "password-reset";
      } catch (Exception e) {
          e.printStackTrace();
          redirectPage = "redirect:/Marchands/connexion";
      }
      return redirectPage;
  }


    /**
     * <p>process called after password is reset</p>
     * @param merchantBean merchant
     * @param theModel modelpage
     * @return modelandview
     */
    @RequestMapping (value = "/Marchands/MotDePasseReset", method = RequestMethod.POST)
    public String resetPassword(@ModelAttribute("merchant") MerchantBean merchantBean, ModelMap theModel){
        int success = 0;
        MerchantBean merchantToresetPassword = merchantsProxy.findMerchantByTokenAndSetsNewPassword(merchantBean.getResetToken(), merchantBean.getPassword());
        success = 1;
        theModel.addAttribute("success", success);
        return "password-reset";
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
        merchantsProxy.deleteMerchant(id);
        session.invalidate();
        return "redirect:/Accueil";
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex){
        return new ModelAndView("redirect:/Marchands/connexion");
    }

    /**
     * <p>Sets session attributes for a merchant</p>
     * @param merchant
     * @param session
     * @return url
     */
    public String setSessionAttributes(MerchantBean merchant, HttpSession session){
        String redirectString = "/Marchands/MonProfil/"+merchant.getId();
        session.setAttribute("loggedInMerchantEmail", merchant.getEmail());
        session.setAttribute("loggedInMerchantId", merchant.getId());
        session.setAttribute("loggedInMerchantRole", merchant.getUserRole().getRoleName());
        return "redirect:"+redirectString;
    }
}