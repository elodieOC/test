package com.mmerchants.microservicemerchants.controller;

import com.mmerchants.microservicemerchants.dao.CategoryDao;
import com.mmerchants.microservicemerchants.dao.MerchantDao;
import com.mmerchants.microservicemerchants.exceptions.CannotAddException;
import com.mmerchants.microservicemerchants.exceptions.NotFoundException;
import com.mmerchants.microservicemerchants.model.*;
import com.mmerchants.microservicemerchants.proxies.MicroserviceRewardsProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Controller for Model Merchant</h2>
 */
@RestController
public class MerchantController {
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private MicroserviceRewardsProxy rewardsProxy;
    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>Lists all merchants</p>
     * @return a list
     */
    @GetMapping(value= "/Marchands")
    public List<Merchant> listMerchants() {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("Listing of all shops");
        return merchantDao.findAll();
    }

     /**
     * <p>Adds a new shop to db</p>
     * @param merchantDTO
     * @return responseEntity
     */
    @PostMapping(value = "/Marchands/add-shop")
    public ResponseEntity<Merchant> addMerchant(@RequestBody MerchantDTO merchantDTO) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        if(merchantDao.findFirstByMerchantName(merchantDTO.getMerchantName()).isPresent()){
            log.error("failure add shop: name already exists");
            throw new CannotAddException("UniqueFail");
        }
        Category cat = categoryDao.getById(merchantDTO.getCategoryId());
        log.info("search by id for category to be linked to shop");
        Merchant toSave = new Merchant(merchantDTO);
        log.info("building shop dto to send image through API");
        toSave.setCategory(cat);
        Merchant merchantAdded =  merchantDao.save(toSave);
        log.info("add shop");
        if (merchantAdded == null) {
            log.error("failure add shop");
            throw new CannotAddException("AddFail");
        }
        log.info("shop added");
        return new ResponseEntity<>(merchantAdded, HttpStatus.CREATED);
    }

    /**
     * <p>shows details of a particular merchant by its id</p>
     * @param id
     * @return the merchant
     */
    @GetMapping(value = "/Marchands/{id}")
    public Optional<Merchant> showMerchant(@PathVariable Integer id) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        return searchOptionalMerchant(id);
    }

    /**
     * Edits shop information if they've been changed
     * @param shop
     * @return user
     */
    @PostMapping(value = "/Marchands/edit")
    ResponseEntity<Merchant> editShop(@RequestBody MerchantDTO shop)  {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Merchant originalShop = merchantDao.findMerchantById(shop.getId());
        log.info("search by id for shop to be modified");
        if(!originalShop.getEmail().equals(shop.getEmail())){
            originalShop.setEmail(shop.getEmail());
            log.info("changing email of shop to be modified");
        }
        if(!originalShop.getAddress().equals(shop.getAddress())){
            originalShop.setAddress(shop.getAddress());
            originalShop.setLatitude(shop.getLatitude());
            originalShop.setLongitude(shop.getLongitude());
            log.info("changing address/lng/lat of shop to be modified");
        }
        if(!originalShop.getMerchantName().equals(shop.getMerchantName())){
            originalShop.setMerchantName(shop.getMerchantName());
            log.info("changing name of shop to be modified");
        }
        if(!originalShop.getMaxPoints().equals(shop.getMaxPoints())){
            originalShop.setMaxPoints( shop.getMaxPoints());
            log.info("changing maxpoints of shop to be modified");
        }
        merchantDao.save(originalShop);
        log.info("shop edited");
        return new ResponseEntity<>(originalShop, HttpStatus.OK);
    }
      /**
     * <p>deletes a merchant from db and all its datas (fidelity cards included)</p>
     * @param id
     */
    @PostMapping(value = "/Marchands/delete/{id}")
    public void deleteShop(@PathVariable Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalMerchant(id);
        Merchant merchantToDelete = merchantDao.getOne(id);
        List<RewardBean> rewards = rewardsProxy.listRewards();
        for (RewardBean r: rewards){
            if(r.getIdMerchant()==merchantToDelete.getId()){
                log.info("shop fidelity card id "+r.getId()+" deleted");
                rewardsProxy.deleteAccount(r.getId());
            }
        }
        merchantDao.delete(merchantToDelete);
        log.info("shop deleted");
    }


    /**
     * Searches for optional Merchant.
     * @param id
     */
    private Optional<Merchant> searchOptionalMerchant(Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<Merchant> shop = merchantDao.findById(id);
        log.info("searching Optional<Merchant> by id");
        if(!shop.isPresent()) {
            log.error("Failure: shop id " + id + " doesn't exist.");
            throw new NotFoundException("La boutique avec l'id " + id + " est INTROUVABLE.");
        }
        return shop;
    }

   
}
