package com.mmerchants.microservicemerchants.controller;

import com.mmerchants.microservicemerchants.dao.CategoryDao;
import com.mmerchants.microservicemerchants.dao.MerchantDao;
import com.mmerchants.microservicemerchants.exceptions.CannotAddException;
import com.mmerchants.microservicemerchants.exceptions.NotFoundException;
import com.mmerchants.microservicemerchants.model.Merchant;
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

    /**
     * <p>Lists all merchants</p>
     * @return a list
     */
    @GetMapping(value= "/Marchands")
    public List<Merchant> listMerchants() {
        return merchantDao.findAll();
    }

     /**
     * <p>Adds a new shop to db</p>
     * @param merchant
     * @return responseEntity
     */
    @PostMapping(value = "/Marchands/add-shop")
    public ResponseEntity<Merchant> addMerchant(@RequestBody Merchant merchant) {
        if(merchantDao.findFirstByMerchantName(merchant.getMerchantName()).isPresent()){
            throw new CannotAddException("UniqueFail");
        }
        Merchant merchantAdded =  merchantDao.save(merchant);
        if (merchantAdded == null) {throw new CannotAddException("AddFail");}
        return new ResponseEntity<Merchant>(merchantAdded, HttpStatus.CREATED);
    }

    /**
     * <p>shows details of a particular merchant by its id</p>
     * @param id
     * @return the merchant
     */
    @GetMapping(value = "/Marchands/{id}")
    public Optional<Merchant> showMerchant(@PathVariable Integer id) {
        Optional<Merchant> merchant = merchantDao.findById(id);
        if(!merchant.isPresent()) {
            throw new NotFoundException("La boutique avec l'id " + id + " est INTROUVABLE.");
        }
        return merchant;
    }

    /**
     * Edits shop information if they've been changed
     * @param shop
     * @return user
     */
    @PostMapping(value = "/Marchands/edit")
    ResponseEntity<Merchant> editShop(@RequestBody Merchant shop)  {
        Merchant originalShop = merchantDao.getOne(shop.getId());
        if(!originalShop.getEmail().equals(shop.getEmail())){
            originalShop.setEmail(shop.getEmail());
        }
        if(!originalShop.getAddress().equals(shop.getAddress())){
            originalShop.setAddress(shop.getAddress());
            originalShop.setLatitude(shop.getLatitude());
            originalShop.setLongitude(shop.getLongitude());
        }
        if(!originalShop.getMerchantName().equals(shop.getMerchantName())){
            originalShop.setMerchantName(shop.getMerchantName());
        }
        if(!originalShop.getMaxPoints().equals(shop.getMaxPoints())){
            originalShop.setMaxPoints( shop.getMaxPoints());
        }
        merchantDao.save(originalShop);
        return new ResponseEntity<Merchant>(originalShop, HttpStatus.OK);
    }
      /**
     * <p>deletes a merchant from db and all its datas</p>
     * @param id
     */
    @PostMapping(value = "/Marchands/delete/{id}")
    public void deleteUSer(@PathVariable Integer id){
        Optional<Merchant> shop = merchantDao.findById(id);
        if(!shop.isPresent()) {
            throw new NotFoundException("La boutique avec l'id " + id + " est INTROUVABLE.");
        }
        Merchant merchantToDelete = merchantDao.getOne(id);
        merchantDao.delete(merchantToDelete);
    }

   
}
