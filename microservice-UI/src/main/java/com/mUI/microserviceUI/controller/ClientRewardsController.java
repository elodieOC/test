package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.MerchantBean;
import com.mUI.microserviceUI.beans.RewardBean;
import com.mUI.microserviceUI.exceptions.CannotAddException;
import com.mUI.microserviceUI.proxies.MicroserviceMerchantsProxy;
import com.mUI.microserviceUI.proxies.MicroserviceRewardsProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static com.mUI.microserviceUI.utils.MapsUtils.setUpForGMaps;

/**
 * <h2>Controller linking with microservice-rewards</h2>
 */
@Controller
public class ClientRewardsController {

    @Autowired
    private MicroserviceRewardsProxy rewardsProxy;
    @Autowired
    private MicroserviceMerchantsProxy merchantsProxy;
    Logger log = LoggerFactory.getLogger(this.getClass());

    /*
     **************************************
     * Reward register
     * ************************************
     */
    /**
     * <p>Process called after the submit button is clicked on register page</p>
     * @param rewardBean reward being created by DTO
     * @param model attribute passed to jsp page
     * @return page to show depending on result of process
     */
    @PostMapping("/CarteFidelites/add-account")
    public String saveReward(@ModelAttribute("rewardCard")RewardBean rewardBean, ModelMap model) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String toBeReturned;
        try {
            log.info("call merchantsProxy");
            rewardBean.setMaxPoints(merchantsProxy.showShop(rewardBean.getIdMerchant()).getMaxPoints());
            // create card
            log.info("call rewardProxy");
            RewardBean rewardToRegister = rewardsProxy.addReward(rewardBean);
            toBeReturned = "redirect:/CarteFidelites/"+rewardToRegister.getId();
        }
        catch (Exception e){
            e.printStackTrace();
            if(e instanceof CannotAddException){
                model.addAttribute("errorMessage", e.getMessage());
            }
            toBeReturned = "redirect:/Marchands/"+rewardBean.getIdMerchant();
        }
        return toBeReturned;
    }

    /**
     * shows details of particular reward with its id
     * @param cardId
     * @param model
     * @return reward-details.html
     */
    @RequestMapping(value={"/CarteFidelites/{cardId}", "/CarteFidelites/{cardId}/code", "/CarteFidelites/{cardId}/reward"})
    public String rewardDetails(@PathVariable Integer cardId, Model model, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        log.info("call rewardProxy");
        RewardBean reward = rewardsProxy.showReward(cardId);
        MerchantBean merchantBean = new MerchantBean();
        log.info("call merchantsProxy");
        List<MerchantBean> merchantBeanList = merchantsProxy.listMerchants();
        for(MerchantBean shop:merchantBeanList){
            if(shop.getId() == reward.getIdMerchant()){
                setUpForGMaps(shop);
                merchantBean = shop;
            }
        }
        model.addAttribute("reward", reward);
        model.addAttribute("merchant", merchantBean);
        model.addAttribute("pointsOn", reward.getPoints());
        model.addAttribute("sessionId", session.getAttribute("loggedInUserId"));
        model.addAttribute("sessionRole", session.getAttribute("loggedInUserRole"));
        return "card-profile";
    }

    /*
     **************************************
     * Reward add points
     * ************************************
     */

    /**
     * <p>When a client goes to the shop, he connects to its profile, goes to shop's card and shows the QR code.</p>
     * <p>The merchant connects to its own profile, and when connected can flash qr code to add a point to the client's card.</p>
     * @param id id of the reward card
     * @param request servlet request
     * @return success or home page
     */
    @RequestMapping(value = "/CarteFidelites/{id}/add-point")
    public String addPoint(@PathVariable("id") Integer id, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String toBeReturned;
        HttpSession session = request.getSession();
        //only a merchant can add a point to a card.

  /*      /*START to delete when not local test
        log.info("call rewardProxy");
        RewardBean rewardBean = rewardsProxy.showReward(id);
        log.info("call rewardProxy");
            rewardsProxy.addPoint(id);
            toBeReturned = "redirect:successPointAdded";
        /*END to delete when not local test */

        // UNCOMMENT when not local tests or development
        if(session.getAttribute("loggedInUserRole").equals("MERCHANT")) {
            RewardBean rewardBean = rewardsProxy.showReward(id);
            MerchantBean m = merchantsProxy.showShop(rewardBean.getIdMerchant());
            if(m.getUserId() == session.getAttribute("loggedInUserId")) {
                log.info("calling rewards proxy");
                rewardsProxy.addPoint(id);
                toBeReturned = "redirect:successPointAdded";
            }
            else{
                log.warn("Le marchand avec l'id "+session.getAttribute("loggedInUserId")+
                        " a essayé d'accéder à une autre boutique");
                toBeReturned="redirect:/Accueil";
                }
            }
        else{
            log.warn("L'utilsiateur avec l'id "+session.getAttribute("loggedInUserId")+
                    " a essayé d'accéder à une boutique sans être marchand");
            toBeReturned="redirect:/Accueil";
        }
        return toBeReturned;
    }


    @GetMapping ("/CarteFidelites/{id}/successPointAdded")
    public String successPointAdded(@PathVariable("id") Integer id, HttpServletRequest request, Model model) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        model.addAttribute("cardId", id);
        return "successPointAdded";
    }

    /*
     **************************************
     * Reward redeem reward
     * ************************************
     */

    /**
     * <p>When a client has enough points, redeems a reward with the points</p>
     * <p>gets a reward and sets points to zero</p>
     * @param id of reward card
     * @param request servlet request
     * @return reward card page or homepage
     */
    @PostMapping(value = "/CarteFidelites/{id}/redeem")
    public String redeem(@PathVariable("id") Integer id, HttpServletRequest request) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String toBeReturned;
        HttpSession session = request.getSession();
        log.info("call rewardProxy");
        RewardBean rewardBean = rewardsProxy.showReward(id);
        if(rewardBean.getIdUser() == session.getAttribute("loggedInUserId")) {
            log.info("call rewardProxy");
            rewardsProxy.redeem(id);
            toBeReturned = "redirect:/CarteFidelites/" + rewardBean.getId();
        }
        else{
            System.out.println("L'utilsiateur avec l'id "+session.getAttribute("loggedInUserId")+
                    " a essayé d'accéder à une récompense qui n'est pas la sienne");
            toBeReturned="redirect:/Accueil";
        }
        return toBeReturned;
    }
    /*
     **************************************
     * Reward delete
     * ************************************
     */

    /**
     * <p>deletes a reward when reward clicks on suppress button</p>
     * @param id
     * @return url depending on function result
     */
    @RequestMapping(value = "/CarteFidelites/delete/{id}", method = RequestMethod.POST)
    public String deleteReward(@PathVariable("id") Integer id, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        log.info("call rewardProxy");
        rewardsProxy.deleteAccount(id);
        return "redirect:/Utilisateurs/MonProfil/"+userId;
    }



    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        return new ModelAndView("redirect:/Accueil");
    }

}