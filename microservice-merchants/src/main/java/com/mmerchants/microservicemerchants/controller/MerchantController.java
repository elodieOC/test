package com.mmerchants.microservicemerchants.controller;

import com.mmerchants.microservicemerchants.dao.MerchantDao;
import com.mmerchants.microservicemerchants.exceptions.BadLoginPasswordException;
import com.mmerchants.microservicemerchants.exceptions.CannotAddException;
import com.mmerchants.microservicemerchants.exceptions.NotFoundException;
import com.mmerchants.microservicemerchants.model.Merchant;
import com.mmerchants.microservicemerchants.utils.Encryption;
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
    @PostMapping(value = "/Marchands/MesBoutiques/add-shop")
    public ResponseEntity<Merchant> addMerchant(@RequestBody Merchant merchant) {
        if(merchantDao.findFirstByMerchantName(merchant.getMerchantName()).isPresent()){
            throw new CannotAddException("Merchant04");
        }
        Merchant merchantAdded =  merchantDao.save(merchant);
        if (merchantAdded == null) {throw new CannotAddException("Merchant03");}
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
     * <p>deletes a merchant from db and all its datas</p>
     * @param id
     */
    @PostMapping(value = "/Marchands/delete/{id}")
    public void deleteUSer(@PathVariable Integer id){
        Optional<Merchant> user = merchantDao.findById(id);
        if(!user.isPresent()) {
            throw new NotFoundException("La boutique avec l'id " + id + " est INTROUVABLE.");
        }
        Merchant merchantToDelete = merchantDao.getOne(id);
        merchantDao.delete(merchantToDelete);
    }

   
}
