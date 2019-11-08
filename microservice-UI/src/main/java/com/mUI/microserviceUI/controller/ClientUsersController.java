package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.MerchantBean;
import com.mUI.microserviceUI.beans.UserBean;
import com.mUI.microserviceUI.exceptions.BadLoginPasswordException;
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
 * <h2>Controller linking with microservice-users</h2>
 */
@Controller
public class ClientUsersController {

    @Autowired
    private MicroserviceUsersProxy usersProxy;
    @Autowired
    private MicroserviceMailingProxy mailingProxy;
    @Autowired
    private MicroserviceMerchantsProxy merchantsProxy;

    /*
     **************************************
     * User register
     * ************************************
     */


    /**
     * <p>Page that displays a form to register a user</p>
     * @param model attribute passed to jsp page
     * @return login page
     */
    @GetMapping("/Utilisateurs/inscription")
    public String registerPage(Model model) {
        UserBean userBean = new UserBean();
        model.addAttribute("user", userBean);
        return "register";
    }

    /**
     * <p>Process called after the submit button is clicked on register page</p>
     * @param user user being created
     * @param request servlet request
     * @param model attribute passed to jsp page
     * @return page to show depending on result of process
     */
    @PostMapping("/Utilisateurs/add-user")
    public String saveUser(@ModelAttribute("user") UserBean user, HttpServletRequest request, ModelMap model) {
        String toBeReturned;
        HttpSession session = request.getSession();
        try {
            UserBean userToRegister = usersProxy.addUser(user);
            toBeReturned = setSessionAttributes(userToRegister, session);
        }
        catch (Exception e){
            e.printStackTrace();
            if(e instanceof CannotAddException){
                model.addAttribute("errorMessage", e.getMessage());
            }
            else{
                model.addAttribute("errorMessage", e.getMessage());
            }
            toBeReturned = "register";
        }
        return toBeReturned;
    }
    /*
     **************************************
     * User login
     * ************************************
     */

    /**
     * <p>Page that displays a form to login a user</p>
     * @param model attribute passed to jsp page
     * @return login page
     */
    @GetMapping("/Utilisateurs/connexion")
    public String loginPage(Model model) {
        UserBean userBean = new UserBean();
        model.addAttribute("user", userBean);
        return "login";
    }


    @RequestMapping("/Utilisateurs/log-user")
    public String logUser(@ModelAttribute("user") UserBean userBean, ModelMap model, HttpServletRequest request) {
        String toBeReturned;
        HttpSession session = request.getSession();
        try{
            UserBean userToConnect = usersProxy.logUser(userBean.getEmail(), userBean.getPassword());
            toBeReturned = setSessionAttributes(userToConnect, session);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("FIELDS: "+userBean.getEmail()+" "+userBean.getPassword());
            if(e instanceof BadLoginPasswordException){
                model.addAttribute("errorMessage", "Login ou Mot de Passe incorrect");
            }
            toBeReturned = "login";
        }
        return toBeReturned;
    }

    /**
     * shows details of particular user with its id
     * @param userId
     * @param model
     * @return user-details.html
     */
    @RequestMapping("/Utilisateurs/MonProfil/{userId}")
    public String userDetails(@PathVariable Integer userId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        UserBean user = usersProxy.showUser(userId);

        if(!session.getAttribute("loggedInUserId").equals(userId)){
            System.out.println("User trying to access profile is not the owner of the profile");
            System.out.println("User is: [id:"
                    +session.getAttribute("loggedInUserId")+ ", email:"
                    +session.getAttribute("loggedInUserEmail")+", role:"
                    +session.getAttribute("loggedInUserRole")+"]");
            return "redirect:/Accueil";
        }
        model.addAttribute("user", user);
        model.addAttribute("sessionRole", session.getAttribute("loggedInUserRole"));
        return "user-profile";
    }
    /*
     **************************************
     * User logout
     * ************************************
     */
    /**
     * Process called after the logout button is clicked in navbar
     * @param session session
     * @return homepage
     */
    @RequestMapping(value = "/Utilisateurs/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/Accueil";
    }

  /*
     **************************************
     * User reset password
     * ************************************
     */

    /**
     *<p>displays form to send an email to retrieve a link to reset password</p>
     * @param model user
     * @return page with form
     */
    @GetMapping(value = "/Utilisateurs/MotDePasseOublie")
    public String forgotPassword(Model model) {
        UserBean userBean = new UserBean();
        model.addAttribute("user", userBean);
        return "password-forgot";
    }

    /**
     *<p>process called when form to send reset link is validated</p>
     * @param userBean user
     * @param theModel form model
     * @param request servlet request
     * @return page form
     */
    @RequestMapping("/Utilisateurs/forgot-password")
    public String findUserSendLinkForPassword(@ModelAttribute("user") UserBean userBean, ModelMap theModel, HttpServletRequest request) {
        UserBean userToFind = new UserBean();
        System.out.println(userBean);
        String appUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
        try{
            userToFind = usersProxy.findUserForPassword(userBean.getEmail());
            theModel.addAttribute("successMessage", "Un email a été envoyé à l'adresse indiquée");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("EMAIL INPUT = "+userBean.getEmail());
            theModel.addAttribute("errorMessage", "email inconnu");
        }
        //TODO le mail est bien envoyé mais renvoi le catch: FeignException: status 504 reading MicroserviceMailingProxy#sendLinkForPassword(String,String,String)
        try{
            mailingProxy.sendLinkForPassword(userToFind.getEmail(), userToFind.getResetToken(), appUrl);
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
  @RequestMapping(value = "/Utilisateurs/MotDePasseReset", method = RequestMethod.GET)
    public String resetPasswordForm(Model model, @RequestParam("token") String token) {
      String redirectPage = "";
      try {
          UserBean userBean = usersProxy.findUserByToken(token);
          model.addAttribute("user", userBean);
          model.addAttribute("resetToken", token);
          redirectPage = "password-reset";
      } catch (Exception e) {
          e.printStackTrace();
          redirectPage = "redirect:/Utilisateurs/connexion";
      }
      return redirectPage;
  }


    /**
     * <p>process called after password is reset</p>
     * @param userBean user
     * @param theModel modelpage
     * @return modelandview
     */
    @RequestMapping (value = "/Utilisateurs/MotDePasseReset", method = RequestMethod.POST)
    public String resetPassword(@ModelAttribute("user") UserBean userBean, ModelMap theModel){
        int success = 0;
        UserBean userToresetPassword = usersProxy.findUserByTokenAndSetsNewPassword(userBean.getResetToken(), userBean.getPassword());
        success = 1;
        theModel.addAttribute("success", success);
        return "password-reset";
    }

    /*
     **************************************
     * User delete
     * ************************************
     */

    /**
     * <p>deletes a user when user clicks on suppress button</p>
     * @param id
     * @return url depending on function result
     */
    @RequestMapping(value = "/Utilisateurs/delete/{id}", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") Integer id, HttpServletRequest request){
        HttpSession session = request.getSession();
        usersProxy.deleteUser(id);
        session.invalidate();
        return "redirect:/Accueil";
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex){
        return new ModelAndView("redirect:/Utilisateurs/connexion");
    }

    /**
     * <p>Sets session attributes for a user</p>
     * @param user
     * @param session
     * @return url
     */
    public String setSessionAttributes(UserBean user, HttpSession session){
        String redirectString = "/Utilisateurs/MonProfil/"+user.getId();
        session.setAttribute("loggedInUserEmail", user.getEmail());
        session.setAttribute("loggedInUserId", user.getId());
        session.setAttribute("loggedInUserRole", user.getUserRole().getRoleName());
        return "redirect:"+redirectString;
    }
}