package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.*;
import com.mUI.microserviceUI.beansDTO.AddShopDTO;
import com.mUI.microserviceUI.beansDTO.EditShopDTO;
import com.mUI.microserviceUI.exceptions.CannotAddException;
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
import java.util.Comparator;
import java.util.List;
import static com.mUI.microserviceUI.utils.MapsUtils.getDistanceDuration;
import static com.mUI.microserviceUI.utils.MapsUtils.setUpForGMaps;

/**
 * <h2>Controller linking with microservice-merchants</h2>
 */
@Controller
public class ClientMerchantsController {

    @Autowired
    private MicroserviceMerchantsProxy merchantsProxy;
    @Autowired
    private MicroserviceRewardsProxy rewardsProxy;
    @Autowired
    private MicroserviceUsersProxy usersProxy;
    Logger log = LoggerFactory.getLogger(this.getClass());


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
    public String listMerchants(Model model,HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Integer userId = (Integer)request.getSession().getAttribute("loggedInUserId");
        List<MerchantBean> merchantBeanList = getMerchantList(request);
        log.info("call merchantProxy");
        List<CategoryBean> cats = merchantsProxy.listCategories();
        model.addAttribute("merchants", merchantBeanList);
        model.addAttribute("cats", cats);
        model.addAttribute("userId", userId);
        return "merchants";
    }
    /**
     * <p>show list of possible merchants by categorie Id</p>
     * @param model
     * @return merchants.html filtered
     */
    @GetMapping("/Marchands/cat/{catId}")
    public String listMerchantsByOneCategory(Model model,HttpServletRequest request, @PathVariable Integer catId){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Integer userId = (Integer)request.getSession().getAttribute("loggedInUserId");
        List<MerchantBean> merchantBeanList = getMerchantList(request);
        log.info("call merchantProxy");
        List<CategoryBean> cats = merchantsProxy.listCategories();
        List<MerchantBean> filteredByCat = new ArrayList<>();
        for (MerchantBean m:merchantBeanList){
            if(m.getCategory().getId()==catId){
                filteredByCat.add(m);
            }
        }
        model.addAttribute("merchants", filteredByCat);
        model.addAttribute("cats", cats);
        model.addAttribute("userId", userId);
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
    public String registerPage(Model model, HttpServletRequest request) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        AddShopDTO addShopDTO = new AddShopDTO();
        HttpSession session = request.getSession();
        log.info("call merchantProxy");
        List<CategoryBean> allCats = merchantsProxy.listCategories();
        model.addAttribute("cats", allCats);
        model.addAttribute("sessionId", session.getAttribute("loggedInUserId"));
        model.addAttribute("addShopDTO", addShopDTO);
        return "register-shop";
    }
    /**
     * <p>Process called after the submit button is clicked on register page</p>
     * @param addShopDTO merchant being created by DTO
     * @param model attribute passed to jsp page
     * @return page to show depending on result of process
     */
    @PostMapping("/Marchands/add-shop")
    public String saveMerchant(@ModelAttribute("addShopDTO")AddShopDTO addShopDTO, ModelMap model) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String toBeReturned;
        String[] longAddress = addShopDTO.getAddress().split(",", addShopDTO.getAddress().length());
        String address=longAddress[0];
        String city = longAddress[1].replaceAll("\\s+","");
        log.info("call merchantProxy");
        List<CategoryBean> allCats = merchantsProxy.listCategories();
        if (!city.equals("Puteaux")){
            log.info("city given in not puteaux");
            model.addAttribute("cats", allCats);
            model.addAttribute("errorMessage", "Cette application supporte actuellement les commerces de Puteaux uniquement");
            return "register-shop";
        }
        if (Integer.parseInt(addShopDTO.getPoints())>10){
            log.info("points given are > 10");
            model.addAttribute("cats", allCats);
            model.addAttribute("errorMessage", "Vous devez attribuer un maximum de 10 points à une carte fidélité");
            return "register-shop";
        }
        try {
            addShopDTO.setMaxPoints(Integer.parseInt(addShopDTO.getPoints()));
            ArrayList<String>longlat=MapsUtils.geocodeFromString(addShopDTO.getAddress());
            addShopDTO.setLongitude(longlat.get(0));
            addShopDTO.setLatitude(longlat.get(1));
            addShopDTO.setAddress(address);
            log.info("call merchantProxy");
            MerchantBean merchantToRegister = merchantsProxy.addShop(addShopDTO);
            toBeReturned = "redirect:/Marchands/"+merchantToRegister.getId();
        }
        catch (Exception e){
            log.error("Failure:merchantsProxy.addShop");
            e.printStackTrace();
            if(e instanceof CannotAddException){
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("cats", allCats);
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        log.info("call merchantProxy");
        MerchantBean merchant = merchantsProxy.showShop(shopId);
        setUpForGMaps(merchant);
        //prepare reward card for merchant
        RewardBean rewardCard = new RewardBean();
        rewardCard.setIdMerchant(merchant.getId());
        rewardCard.setMaxPoints(merchant.getMaxPoints());
        Integer userId = (Integer)request.getSession().getAttribute("loggedInUserId");
        rewardCard.setIdUser(userId);
        log.info("call merchantProxy");
        for(RewardBean r:rewardsProxy.listRewards()){
            if(r.getIdUser() == rewardCard.getIdUser()){
                if (r.getIdMerchant() == rewardCard.getIdMerchant()){
                    model.addAttribute("ownerCardId", rewardCard.getId());
                }
            }
        }
        //if a user is logged in, sets up DistanceMatrix attributes from the user's address
        if(userId!=null) {
            log.info("call usersProxy");
            merchant.setDm(getDistanceDuration(usersProxy.showUser(userId),merchant));
        }
        //if user in session is owner of shops, show the users with cards of the shops
        if(merchant.getUserId() == userId){
            //search if shop has active fidelity cards
            log.info("call rewardsProxy");
            List<RewardBean>rewards = rewardsProxy.listRewards();
            List<RewardBean>shopRewards = new ArrayList<>();
            for (RewardBean r:rewards){
                if(r.getIdMerchant()==shopId){
                    shopRewards.add(r);
                }
            }
            //search number of total rewards in waiting
            int rewardsInWaiting = 0;
            for(RewardBean r: shopRewards){
                int cardRewards = r.getRewardsNbr();
                rewardsInWaiting += cardRewards;
            }
            model.addAttribute("cards", shopRewards);
            model.addAttribute("rewardsTot", rewardsInWaiting);
        }
        model.addAttribute("merchant", merchant);
        model.addAttribute("rewardCard", rewardCard);
        model.addAttribute("sessionId", session.getAttribute("loggedInUserId"));
        return "shop-profile";
    }
    /**
     * shows lists of shop with its owner id
     * @param model
     * @return owner's shops sorted by id
     */
    @RequestMapping("/Marchands/MesBoutiques")
    public String shopListByOwner(Model model, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("call merchantProxy");
        List<MerchantBean> allShops = merchantsProxy.listMerchants();
        Integer loggedInUserId = (Integer)request.getSession().getAttribute("loggedInUserId");
        List<MerchantBean> list = new ArrayList<>();
        for(MerchantBean shop:allShops){
            if(loggedInUserId.equals(shop.getUserId())){
                //set up google map
                setUpForGMaps(shop);
                //set up category icon
                log.info("call merchantProxy");
                shop.getCategory().setIcon(merchantsProxy.getCategoryIcon(shop.getCategory().getCategoryIcon()));
                list.add(shop);
            }
        }
        list.sort(Comparator.comparing(MerchantBean::getId));
        model.addAttribute("shopList", list);
        return "myshops";
    }
    /*
     **************************************
     * Edit Merchant
     * ************************************
     */
    @GetMapping("/Marchands/edit/{shopId}")
    public String editUserPage(@PathVariable Integer shopId, Model model, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        log.info("call merchantProxy");
        MerchantBean shop = merchantsProxy.showShop(shopId);
        //session is always checked, here check if user editing profile is owner of profile
        if(!session.getAttribute("loggedInUserId").equals(shop.getUserId())){
            log.warn("User trying to edit shop profile is not the owner of the shop");
            log.warn("User is: [id:"
                    +session.getAttribute("loggedInUserId")+ ", email:"
                    +session.getAttribute("loggedInUserEmail")+", role:"
                    +session.getAttribute("loggedInUserRole")+"]");
            return "redirect:/Accueil";
        }
        model.addAttribute("shop", shop);
        model.addAttribute("shopId", shop.getId());
        return "edit-shop";
    }

    @RequestMapping("/Marchands/edit")
    public String editUser(@ModelAttribute("shop") EditShopDTO editShopDTO, RedirectAttributes atts, ModelMap model, HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String toBeReturned;
        HttpSession session = request.getSession();
        log.info("call merchantProxy");
        MerchantBean shop = merchantsProxy.showShop(editShopDTO.getId());
        String[] longAddress = editShopDTO.getAddress().split(",", editShopDTO.getAddress().length());
        String address=longAddress[0];
        String city = longAddress[1].replaceAll("\\s+","");
        if (!city.equals("Puteaux")){
            model.addAttribute("errorMessage", "Cette application supporte actuellement les commerces de Puteaux uniquement");
            toBeReturned = "redirect:/Marchands/edit/"+editShopDTO.getId();
        }
        if(!editShopDTO.getEmail().isEmpty()){
            shop.setEmail(editShopDTO.getEmail());
        }if(!editShopDTO.getAddress().isEmpty()){
            shop.setAddress(address);
            ArrayList<String>longlat=MapsUtils.geocodeFromString(editShopDTO.getAddress());
            shop.setLongitude(longlat.get(0));
            shop.setLatitude(longlat.get(1));
        }if(!editShopDTO.getMerchantName().isEmpty()){
            shop.setMerchantName(editShopDTO.getMerchantName());
        }if(!editShopDTO.getMaxPoints().isEmpty()){
            shop.setMaxPoints(Integer.parseInt(editShopDTO.getMaxPoints()));
        }
        try{
            log.info("call merchantProxy");
            MerchantBean shopToEdit = merchantsProxy.editShop(shop);
            toBeReturned = "redirect:/Marchands/"+shopToEdit.getId();
        }catch (Exception e){
            e.printStackTrace();
            log.error("Failure merchantsProxy.editshop");
            atts.addFlashAttribute("errorMessage", "Erreur dans la modification de la boutique");
            toBeReturned = "redirect:/Marchands/edit/"+editShopDTO.getId();
        }
        return toBeReturned;
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        log.info("call merchantProxy");
        merchantsProxy.deleteShop(id);
        return "redirect:/Marchands/MesBoutiques";
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        return new ModelAndView("redirect:/Accueil");
    }

    /**
     * function used in routes /Marchands and /Marchands/cat/{catId}
     * gets list of shops and sets google map an category icon
     * if a user is logged in, adds the distance information to it and sort by closest distance to user's address
     * @param request
     * @return list of shops (sorted by distance if a user is logged in/or by id=>created first)
     */
    private List<MerchantBean> getMerchantList(HttpServletRequest request){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Integer userId = (Integer)request.getSession().getAttribute("loggedInUserId");
        log.info("call merchantProxy");
        List<MerchantBean> merchantBeanList = merchantsProxy.listMerchants();
        for (MerchantBean m:merchantBeanList){
            //set up google map
            setUpForGMaps(m);
            //set up category icon
            log.info("call merchantProxy");
            m.getCategory().setIcon(merchantsProxy.getCategoryIcon(m.getCategory().getCategoryIcon()));
            //if a user is logged in, sets up DistanceMatrix attributes from the user's address
            if(userId!=null) {
                log.info("call usersProxy");
                m.setDm(getDistanceDuration(usersProxy.showUser(userId),m));
                m.setDurationValue(m.getDm().getDurationValue());
            }
        }
        if(userId!=null){
            merchantBeanList.sort(Comparator.comparing(MerchantBean::getDurationValue));
        }
        else{
            merchantBeanList.sort(Comparator.comparing(MerchantBean::getId));
        }
        return merchantBeanList;
    }
}