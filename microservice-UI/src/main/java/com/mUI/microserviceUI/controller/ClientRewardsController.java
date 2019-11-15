package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.MerchantBean;
import com.mUI.microserviceUI.beans.RewardBean;
import com.mUI.microserviceUI.exceptions.CannotAddException;
import com.mUI.microserviceUI.proxies.MicroserviceMerchantsProxy;
import com.mUI.microserviceUI.proxies.MicroserviceRewardsProxy;
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
 * <h2>Controller linking with microservice-rewards</h2>
 */
@Controller
public class ClientRewardsController {

    @Autowired
    private MicroserviceRewardsProxy rewardsProxy;
    @Autowired
    private MicroserviceMerchantsProxy merchantsProxy;

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
        String toBeReturned;
        try {
            rewardBean.setMaxPoints(merchantsProxy.showShop(rewardBean.getIdMerchant()).getMaxPoints());
            // create card
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
    @RequestMapping("/CarteFidelites/{cardId}")
    public String rewardDetails(@PathVariable Integer cardId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        RewardBean reward = rewardsProxy.showReward(cardId);
        MerchantBean merchantBean = new MerchantBean();
        List<MerchantBean> merchantBeanList = merchantsProxy.listMerchants();
        for(MerchantBean shop:merchantBeanList){
            if(shop.getId() == reward.getIdMerchant()){
                merchantBean = shop;
            }
        }
        model.addAttribute("reward", reward);
        model.addAttribute("merchant", merchantBean);
        model.addAttribute("sessionId", session.getAttribute("loggedInUserId"));
        model.addAttribute("sessionRole", session.getAttribute("loggedInUserRole"));
        return "card-profile";
    }

    /*
     **************************************
     * Reward add points
     * ************************************
     */
    @PostMapping(value = "/CarteFidelites/{id}/add-point")
    public String addPoint(@PathVariable("id") Integer id, HttpServletRequest request){
        String toBeReturned;
        HttpSession session = request.getSession();
        /*to delete when not local test */
        RewardBean rewardBean = rewardsProxy.showReward(id);
            rewardsProxy.addPoint(id);
            toBeReturned = "redirect:/CarteFidelites/" + id;


        /* commented for test local reasons
        if(session.getAttribute("loggedInUserRole").equals("MERCHANT")) {
            RewardBean rewardBean = rewardsProxy.showReward(id);
            if(rewardBean.getIdMerchant() == session.getAttribute("loggedInUserId")) {
                rewardsProxy.addPoint(id);
                toBeReturned = "redirect:/CarteFidelites/" + id;
            }
            else{//TODO ajouter sout pour qui a fait quoi
                System.out.println("Le marchand avec l'id "+session.getAttribute("loggedInUserId")+
                        " a essayé d'accéder à une autre boutique");
                toBeReturned="redirect:/Accueil";
                }
            }
        else{//TODO ajouter sout pour qui a fait quoi
            System.out.println("L'utilsiateur avec l'id "+session.getAttribute("loggedInUserId")+
                    " a essayé d'accéder à une boutique sans être marchand");
            toBeReturned="redirect:/Accueil";
        }*/
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
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        rewardsProxy.deleteAccount(id);
        return "redirect:/Utilisateurs/MonProfil/"+userId;
    }



    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex){
        return new ModelAndView("redirect:/Accueil");
    }

}