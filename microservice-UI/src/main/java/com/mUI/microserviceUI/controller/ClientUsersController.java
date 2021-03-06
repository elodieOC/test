package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.*;
import com.mUI.microserviceUI.beansDTO.EditUserDTO;
import com.mUI.microserviceUI.exceptions.BadLoginPasswordException;
import com.mUI.microserviceUI.exceptions.CannotAddException;
import com.mUI.microserviceUI.proxies.MicroserviceMailingProxy;
import com.mUI.microserviceUI.proxies.MicroserviceMerchantsProxy;
import com.mUI.microserviceUI.proxies.MicroserviceRewardsProxy;
import com.mUI.microserviceUI.proxies.MicroserviceUsersProxy;
import com.mUI.microserviceUI.utils.MapsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import static com.mUI.microserviceUI.utils.MapsUtils.getDistanceDuration;

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
    @Autowired
    private MicroserviceRewardsProxy rewardsProxy;
    Logger log = LoggerFactory.getLogger(this.getClass());


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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String toBeReturned;
        HttpSession session = request.getSession();
        try {
            String[] longAddress = user.getAddress().split(",", user.getAddress().length());
            String address=longAddress[0];
            String city = longAddress[1].replaceAll("\\s+","");
            if (!city.equals("Puteaux")){
                log.info("city given in not puteaux");
                model.addAttribute("errorMessage", "Cette application supporte actuellement les commerces de Puteaux uniquement");
                return "register";
            }
            user.setAddress(address+", "+city);
            ArrayList<String>longlat=MapsUtils.geocodeFromString(user.getAddress());
            user.setLongitude(longlat.get(0));
            user.setLatitude(longlat.get(1));
            log.info("call usersProxy");
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        UserBean userBean = new UserBean();
        model.addAttribute("user", userBean);
        return "login";
    }

    @RequestMapping("/Utilisateurs/log-user")
    public String logUser(@ModelAttribute("user") UserBean userBean, ModelMap model, HttpServletRequest request) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String toBeReturned;
        HttpSession session = request.getSession();
        try{
            log.info("call usersProxy");
            UserBean userToConnect = usersProxy.logUser(userBean.getEmail(), userBean.getPassword());
            toBeReturned = setSessionAttributes(userToConnect, session);
        }catch (Exception e){
            log.error("Failure logging user");
            e.printStackTrace();
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
    @RequestMapping(value = {"/Utilisateurs/MonProfil/{userId}", "/Utilisateurs/MonProfil/{userId}/mescartes"})
    public String userDetails(@PathVariable Integer userId, Model model, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        log.info("call usersProxy");
        UserBean user = usersProxy.showUser(userId);
        //session is always checked, here check if user accessing profile is owner of profile
        if(!session.getAttribute("loggedInUserId").equals(userId)){
            log.warn("User trying to access profile is not the owner of the profile");
            log.warn("User is: [id:"
                    +session.getAttribute("loggedInUserId")+ ", email:"
                    +session.getAttribute("loggedInUserEmail")+", role:"
                    +session.getAttribute("loggedInUserRole")+"]");
            return "redirect:/Accueil";
        }
        //adds the user's fidelity cards
        log.info("call rewardsProxy");
        List<RewardBean> rewardBeans = rewardsProxy.listRewards();
        List<RewardBean> userRewards = new ArrayList<>();
        for (RewardBean reward : rewardBeans){
            if(reward.getIdUser() == userId){
                userRewards.add(reward);
            }
        }
        if(!userRewards.isEmpty()) {
            List<MerchantBean> myRewardingShops = new ArrayList<>();
            for (RewardBean r : userRewards) {
                log.info("call merchantsProxy");
                    MerchantBean m = merchantsProxy.showShop(r.getIdMerchant());
                    //set up google map
                    m.setMapsAddress(MapsUtils.setUrlAddressForMapsAPI(m.getAddress()));
                    //set up category icon
                    log.info("call merchantsProxy");
                    m.getCategory().setIcon(merchantsProxy.getCategoryIcon(m.getCategory().getCategoryIcon()));
                    //set up distance/duration datas
                    log.info("call usersProxy");
                    m.setDm(getDistanceDuration(usersProxy.showUser(userId), m));
                    myRewardingShops.add(m);
            }
            model.addAttribute("shops", myRewardingShops);
        }
        model.addAttribute("user", user);
        model.addAttribute("userRewards", userRewards);
        model.addAttribute("sessionRole", session.getAttribute("loggedInUserRole"));
        return "user-profile";
    }

    /*
     **************************************
     * Edit User
     * ************************************
     */
    @GetMapping("/Utilisateurs/MonProfil/edit/{id}")
    public String editUserPage(@PathVariable Integer id, Model model, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        log.info("call usersProxy");
        UserBean user = usersProxy.showUser(id);
        //session is always checked, here check if user editing profile is owner of profile
        if(!session.getAttribute("loggedInUserId").equals(id)){
            log.warn("User trying to edit profile is not the owner of the profile");
            log.warn("User is: [id:"
                    +session.getAttribute("loggedInUserId")+ ", email:"
                    +session.getAttribute("loggedInUserEmail")+", role:"
                    +session.getAttribute("loggedInUserRole")+"]");
            return "redirect:/Accueil";
        }
        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());
        return "edit-user";
    }

    @RequestMapping("/Utilisateurs/edit")
    public String editUser(@ModelAttribute("user") EditUserDTO editUserDTO, ModelMap model, RedirectAttributes atts, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String toBeReturned;
        HttpSession session = request.getSession();
        log.info("call usersProxy");
        UserBean user = usersProxy.showUser(editUserDTO.getId());
        if(!editUserDTO.getEmail().isEmpty()){
            user.setEmail(editUserDTO.getEmail());
        }if(!editUserDTO.getPassword().isEmpty()){
            user.setPassword(editUserDTO.getPassword());
        }if(!editUserDTO.getFirstName().isEmpty()){
            user.setFirstName(editUserDTO.getFirstName());
        }if(!editUserDTO.getLastName().isEmpty()){
            user.setLastName(editUserDTO.getLastName());
        }if(!editUserDTO.getAddress().isEmpty()){
            user.setAddress(editUserDTO.getAddress());
            ArrayList<String>longlat=MapsUtils.geocodeFromString(user.getAddress());
            user.setLongitude(longlat.get(0));
            user.setLatitude(longlat.get(1));
        }
        try{
            log.info("call usersProxy");
            UserBean userToEdit = usersProxy.editUser(user);
            toBeReturned = setSessionAttributes(userToEdit, session);
        }catch (Exception e){
            e.printStackTrace();
            atts.addFlashAttribute("errorMessage", "Erreur dans la modification");
            toBeReturned = "redirect:/Utilisateurs/MonProfil/edit/"+editUserDTO.getId();
        }
        return toBeReturned;
    }


    /*
     **************************************
     * Newsletter
     * ************************************
     */
    /**
     * Contact page and newsletter subscription
     * @param model
     * @return
     */
    @GetMapping("/contact")
    public String contact(Model model){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        UserBean userBean = new UserBean();
        model.addAttribute("user", userBean);
        return "contact";
    }

    /**
     * Process called after user registers for newsletter
     * @return contact page
     */
    @RequestMapping(value = "/contact")
    public String registerNewsletter(@ModelAttribute("newsletter") NewsletterBean newsletter, Model model) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("call usersProxy");
        List<NewsletterBean> newsletterBeanList = usersProxy.listNewsletters();
        for(NewsletterBean n:newsletterBeanList){
            if(newsletter.getEmail().equals(n.getEmail())){
                    model.addAttribute("infoMessage", "Vous êtes déjà inscrit à la newsletter");
                    return "contact";
            }
        }
        try{
            log.info("call usersProxy");
            NewsletterBean newsletterBean = usersProxy.addUserToNewsletter(newsletter);
            String successMessage = "Vous êtes inscrit à la Newsletter";
            model.addAttribute("successMessage", successMessage);
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "contact";
    }

    /**
     * From user profile, adds user to Newsletter
     * @param userId
     * @return profile
     */
    @RequestMapping("/Utilisateurs/MonProfil/{userId}/suscribe")
    public String suscribeUserFromProfile(@PathVariable Integer userId){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("call usersProxy");
        UserBean user = usersProxy.showUser(userId);
        NewsletterBean newsletterBean = new NewsletterBean();
        newsletterBean.setEmail(user.getEmail());
        log.info("call usersProxy");
        usersProxy.addUserToNewsletter(newsletterBean);
        return "redirect:/Utilisateurs/MonProfil/"+userId;
    }

    /**
     * From user profile, unsuscribes user from Newsletter
     * @param userId
     * @return profile
     */
    @RequestMapping("/Utilisateurs/MonProfil/{userId}/unsuscribe")
    public String unsuscribeUserFromProfile(@PathVariable Integer userId){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("call usersProxy");
        UserBean user = usersProxy.showUser(userId);
        log.info("call usersProxy");
        UserBean updatedUser = usersProxy.unsuscribe(user);
        return "redirect:/Utilisateurs/MonProfil/"+updatedUser.getId();
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        session.invalidate();
        log.info("session invalidated");
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        UserBean userToFind = new UserBean();
        String appUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
        try{
            log.info("call usersProxy");
            userToFind = usersProxy.findUserForPassword(userBean.getEmail());
            log.info("call mailingProxy");
            mailingProxy.sendLinkForPassword(userToFind.getEmail(), userToFind.getResetToken(), appUrl);
            theModel.addAttribute("successMessage", "Un email a été envoyé à l'adresse indiquée");
        }catch (Exception e){
            e.printStackTrace();
            log.error("Failure:email unknown "+userBean.getEmail());
            theModel.addAttribute("errorMessage", "email inconnu");
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
      log.info(new Object(){}.getClass().getEnclosingMethod().getName());
      String redirectPage = "";
      try {
          log.info("call usersProxy");
          UserBean userBean = usersProxy.findUserByToken(token);
          model.addAttribute("user", userBean);
          model.addAttribute("resetToken", token);
          redirectPage = "password-reset";
      } catch (Exception e) {
          e.printStackTrace();
          log.error("Failure reset password");
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        int success = 0;
        log.info("call usersProxy");
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

    //TODO ajouter message alerte voulez vous vraiment suprimer?
    /**
     * <p>deletes a user when user clicks on suppress button</p>
     * @param id
     * @return url depending on function result
     */
    @RequestMapping(value = "/Utilisateurs/delete/{id}", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") Integer id, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        log.info("call usersProxy");
        UserBean user = usersProxy.showUser(id);
        List<MerchantBean> userShops=new ArrayList<>();
        List<RewardBean> userCards=new ArrayList<>();
        //If user to delete is merchant, search for possible shops to delete as well
        if(user.getUserRole().getId()==3){
            log.info("call merchantsProxy");
            List<MerchantBean> allShops = merchantsProxy.listMerchants();
            for (MerchantBean m :allShops) {
                if(m.getUserId() == user.getId()){
                    userShops.add(m);
                }
            }
        }
        //search for fidelity cards to delete as well
        log.info("call rewardsProxy");
        List<RewardBean> allRewards = rewardsProxy.listRewards();
        for(RewardBean r: allRewards){
            if(r.getIdUser() == user.getId()){
                userCards.add(r);
            }
        }
        try {
            log.info("call usersProxy");
            usersProxy.deleteUser(id);
            //if user owns shops, delete shops
            if (!userShops.isEmpty()){
                for(MerchantBean m:userShops){
                    log.info("call merchantsProxy");
                    merchantsProxy.deleteShop(m.getId());
                }
            }
            //if user owns cards, delete cards
            if(!userCards.isEmpty()){
                for (RewardBean r:userCards){
                    log.info("call rewardProxy");
                    rewardsProxy.deleteAccount(r.getId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        session.invalidate();
        return "redirect:/Accueil";
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        return new ModelAndView("redirect:/Utilisateurs/connexion");
    }

    /**
     * <p>Sets session attributes for a user</p>
     * @param user
     * @param session
     * @return url
     */
    public String setSessionAttributes(UserBean user, HttpSession session){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String redirectString = "/Utilisateurs/MonProfil/"+user.getId();
        session.setAttribute("loggedInUserEmail", user.getEmail());
        session.setAttribute("loggedInUserId", user.getId());
        session.setAttribute("loggedInUserRole", user.getUserRole().getRoleName());
        return "redirect:"+redirectString;
    }
}