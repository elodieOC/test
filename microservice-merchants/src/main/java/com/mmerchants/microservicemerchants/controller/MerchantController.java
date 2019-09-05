package com.mmerchants.microservicemerchants.controller;

import com.mmerchants.microservicemerchants.dao.MerchantDao;
import com.mmerchants.microservicemerchants.exceptions.BadLoginPasswordException;
import com.mmerchants.microservicemerchants.exceptions.CannotAddException;
import com.mmerchants.microservicemerchants.exceptions.NotFoundException;
import com.mmerchants.microservicemerchants.model.Merchant;
import com.mmerchants.microservicemerchants.utils.Encryption;
import com.mmerchants.microservicemerchants.utils.validators.MerchantLoginValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <h2>Controller for Model Merchant</h2>
 */
@RestController
public class MerchantController {
    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private MerchantLoginValidator merchantLoginValidator;
    /**
     * <p>Lists all merchants</p>
     * @return a list
     */
    @GetMapping(value= "/Marchands")
    public List<Merchant> listMerchants() {
        return merchantDao.findAll();
    }

    /**
     * <p>Adds a new merchant to db, encrypts password before save</p>
     * @param merchant
     * @return responseEntity
     */
    @PostMapping(value = "/Marchands/add-merchant")
    public ResponseEntity<Merchant> addMerchant(@RequestBody Merchant merchant) {
        merchant.setPassword(Encryption.encrypt(merchant.getPassword()));
        Merchant merchantAdded =  merchantDao.save(merchant);
        if (merchantAdded == null) {throw new CannotAddException("Merchant03");}
        return new ResponseEntity<Merchant>(merchantAdded, HttpStatus.CREATED);
    }


    /**
     * <p>shows details of a particular merchant by its id</p>
     * @param id
     * @return the merchant
     */
    @GetMapping(value = "/Marchands/MonProfil/{id}")
    public Optional<Merchant> showMerchant(@PathVariable Integer id) {
        Optional<Merchant> merchant = merchantDao.findById(id);
        if(!merchant.isPresent()) {
            throw new NotFoundException("L'marchand avec l'id " + id + " est INTROUVABLE.");
        }
        return merchant;
    }

    /**
     * <p>method to log merchants</p>
     * @param merchantName from form
     * @param password from form
     * @return response entity of merchant
     */
    @PostMapping(value = "/Marchands/log-merchant")
    public ResponseEntity<Merchant> logMerchant(@RequestParam String merchantName, @RequestParam String password) {
        Merchant merchantLogged =  merchantDao.findByMerchantName(merchantName);
        if (merchantLogged == null) {throw new BadLoginPasswordException("Merchant01");}

        String loginPassword = Encryption.encrypt(password);
        if (!loginPassword.equals(merchantLogged.getPassword())) {
            throw new BadLoginPasswordException("Merchant02");
        }
        return new ResponseEntity<Merchant>(merchantLogged, HttpStatus.OK);
    }

    /**
     * <p>finds a merchant by mail to reset a password (sets a token in db)</p>
     * @param email from form
     * @return a merchant
     */
    @PostMapping(value = "/Marchands/forgot-password")
    public Merchant findMerchantForPassword(@RequestParam String email) {
        try{
            Merchant merchantToFind = merchantDao.findByEmail(email);
            Optional<Merchant> merchant = merchantDao.findById(merchantToFind.getId());
            if(!merchant.isPresent()) {
                throw new NotFoundException("L'marchand avec l'id " + merchantToFind.getId() + " est INTROUVABLE.");
            }
            merchantToFind.setResetToken(UUID.randomUUID().toString());
            //set token valid for 1 day
            merchantToFind.setTokenDate(new Timestamp(System.currentTimeMillis()));
            merchantDao.save(merchantToFind);
            return merchantToFind;
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("Le marchand avec l'email " + email + " est INTROUVABLE.");
        }
    }

    /**
     * <p>finds a merchant by token set to reset a password</p>
     * @param token
     * @return a merchant
     */
    @GetMapping (value = "/Marchands/MotDePasseReset")
    public ResponseEntity<Merchant> findMerchantByToken(@RequestParam String token) {
            Optional<Merchant> merchant = merchantDao.findByResetToken(token);
            if(!merchant.isPresent()) {
                throw new NotFoundException("Le marchand avec le token " + token+ " est INTROUVABLE.");
            }
            Merchant merchantToFind = merchant.get();
        return new ResponseEntity<Merchant>(merchantToFind, HttpStatus.OK);
    }

    /**
     * <p>resets a merchant's password</p>
     * @param token reset token
     * @param password new password
     * @return merchant
     */
    @PostMapping(value = "/Marchands/MotDePasseReset")
    public Optional<Merchant> findMerchantByTokenAndSetsNewPassword(@RequestParam String token, @RequestParam String password) {
        Optional<Merchant> merchant = merchantDao.findByResetToken(token);
        if(!merchant.isPresent()) {
            throw new NotFoundException("Le marchand avec le token " + token+ " est INTROUVABLE.");
        }
        else {
            Merchant resetMerchant = merchant.get();
            resetMerchant.setPassword(Encryption.encrypt(password));
            resetMerchant.setResetToken(null);
            resetMerchant.setTokenDate(null);
            merchantDao.save(resetMerchant);
        }
        return merchant;
    }


   
}
