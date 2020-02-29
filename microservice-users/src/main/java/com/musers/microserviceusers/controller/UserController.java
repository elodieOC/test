package com.musers.microserviceusers.controller;

import com.musers.microserviceusers.dao.NewsletterDao;
import com.musers.microserviceusers.dao.RoleDao;
import com.musers.microserviceusers.dao.UserDao;
import com.musers.microserviceusers.exceptions.BadLoginPasswordException;
import com.musers.microserviceusers.exceptions.CannotAddException;
import com.musers.microserviceusers.exceptions.NotFoundException;
import com.musers.microserviceusers.model.Newsletter;
import com.musers.microserviceusers.model.User;
import com.musers.microserviceusers.utils.Encryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <h2>Controller for Model User</h2>
 */
@RestController
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private NewsletterDao newsletterDao;

    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>Lists all users</p>
     * @return a list
     */
    @GetMapping(value="/Utilisateurs")
    public List<User> listUsers() {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("Listing of all users");
        return userDao.findAll();
    }  /**

     * <p>Lists all newsletter suscribers</p>
     * @return a list
     */
    @GetMapping(value="/Newsletters")
    public List<Newsletter> listNewsletters() {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("Listing of all newsletter subscriptions");
        return newsletterDao.findAll();
    }

    /**
     * <p>Adds a new user to db, encrypts password before save</p>
     * @param user
     * @return responseEntity
     */
    @PostMapping(value = "/Utilisateurs/add-user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        if(userDao.findFirstByEmail(user.getEmail()).isPresent()){
            log.error("failure add user: email already exists");
            throw new CannotAddException("UniqueFail");
        }
        user.setPassword(Encryption.encrypt(user.getPassword()));
        log.info("encryption of password");
        if(user.isMerchantOrNot() == true){
            user.setUserRole(roleDao.getOne(3));
            log.info("set user role to merchant");
        }
        else {
            user.setUserRole(roleDao.getOne(2));
            log.info("set user role to user");
        }
        Optional<Newsletter> newsletter = newsletterDao.findFirstByEmail(user.getEmail());
        if(newsletter.isPresent()){
            log.info("user is already suscribed to newsletter");
            user.setNewsletterSuscriber(true);
        }
        log.info("user is not suscribed to newsletter");
        User userAdded =  userDao.save(user);
        log.info("add user");
        if (userAdded == null) {
            log.error("failure add user");
            throw new CannotAddException("AddFail");
        }
        log.info("user added");
        return new ResponseEntity<>(userAdded, HttpStatus.CREATED);
    }


    /**
     * <p>shows details of a particular user by its id</p>
     * @param id
     * @return the user
     */
    @GetMapping(value = "/Utilisateurs/MonProfil/{id}")
    public Optional<User> showUser(@PathVariable Integer id) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        return searchOptionalUser(id);
    }


    /**
     * <p>method to log users</p>
     * @param email from form
     * @param password from form
     * @return response entity of user
     */
    @PostMapping(value = "/Utilisateurs/log-user")
    public ResponseEntity<User> logUser(@RequestParam String email, @RequestParam String password) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        User userLogged =  userDao.findByEmail(email);
        log.info("search by email for user to be logged in");
        if (userLogged == null) {
            log.error("Failure: email not found");
            throw new BadLoginPasswordException("NotFound");
        }
        String loginPassword = Encryption.encrypt(password);
        log.info("encryption of password");
        if (!loginPassword.equals(userLogged.getPassword())) {
            log.error("Failure: email/password don't match");
            throw new BadLoginPasswordException("LoginPassword");
        }
        log.info("user logged in");
        return new ResponseEntity<>(userLogged, HttpStatus.OK);
    }

    /**
     * Edits user information if they've been changed
     * @param user
     * @return user
     */
    @PostMapping(value = "/Utilisateurs/edit")
    ResponseEntity<User> editUser(@RequestBody User user)  {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        User originalUser = userDao.getOne(user.getId());
        log.info("search by id for user to be modified");
        if(!originalUser.getEmail().equals(user.getEmail())){
            originalUser.setEmail(user.getEmail());
            log.info("changing email of user to be modified");
        }
        if(!originalUser.getFirstName().equals(user.getFirstName())){
            originalUser.setFirstName(user.getFirstName());
            log.info("changing first name of user to be modified");
        }
        if(!originalUser.getLastName().equals(user.getLastName())){
            originalUser.setLastName(user.getLastName());
            log.info("changing last name of user to be modified");
        }
        if(!originalUser.getPassword().equals(user.getPassword())){
            originalUser.setPassword( Encryption.encrypt(user.getPassword()));
            log.info("changing password of user to be modified and encrypts it ");
        }
        if(!originalUser.getAddress().equals(user.getAddress())){
            originalUser.setAddress( user.getAddress());
            originalUser.setLatitude(user.getLatitude());
            originalUser.setLongitude(user.getLongitude());
            log.info("changing address/lng/lat of user to be modified");
        }
        userDao.save(originalUser);
        log.info("user edited");
        return new ResponseEntity<>(originalUser, HttpStatus.OK);
    }

    /**
     * Adds a new suscriber to the newsletter
     * @param newsletter
     * @return Newsletter
     */
    @PostMapping(value = "/Utilisateurs/suscribe")
    public ResponseEntity<Newsletter> addUsertoNewsletter(@RequestBody Newsletter newsletter){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<User> user = userDao.findFirstByEmail(newsletter.getEmail());
        log.info("searching Optional<User> by email");
        if(user.isPresent()) {
            log.info("Optional<User> found");
            User userToSuscribe = userDao.findByEmail(newsletter.getEmail());
            userToSuscribe.setNewsletterSuscriber(true);
            log.info("suscribe user to newsletter");
            userDao.save(userToSuscribe);
        }
        Newsletter news =  newsletterDao.save(newsletter);
        if (news == null) {
            log.error("Failure saving user");
            throw new CannotAddException("AddFail");
        }
        log.info("user saved");
        return new ResponseEntity<>(news, HttpStatus.CREATED);
    }

    /**
     * Unsuscribe a user from the newsletter
     * @param user
     * @return user
     */
    @PostMapping(value = "/Utilisateurs/unsuscribe")
    public ResponseEntity<User> unsuscribe(@RequestBody User user){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalUser(user.getId());
        User userToUnsuscribe = userDao.getOne(user.getId());
        log.info("search by id for user to unsuscribe");
        userToUnsuscribe.setNewsletterSuscriber(false);
        log.info("unsuscribe user to newsletter");
        userDao.save(userToUnsuscribe);
        log.info("user saved");
        Newsletter news = newsletterDao.findByEmail(userToUnsuscribe.getEmail());
        log.info("search by email for newsletter subscrition");
        newsletterDao.delete(news);
        log.info("subscription deleted");
        return new ResponseEntity<>(userToUnsuscribe, HttpStatus.OK);
    }
    /**
     * <p>finds a user by mail to reset a password (sets a token in db)</p>
     * @param email from form
     * @return a user
     */
    @PostMapping(value = "/Utilisateurs/forgot-password")
    public User findUserForPassword(@RequestParam String email) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        try{
            User userToFind = userDao.findByEmail(email);
            log.info("search by email for user");
            searchOptionalUser(userToFind.getId());
            userToFind.setResetToken(UUID.randomUUID().toString());
            log.info("set resetToken to user");
            //set token valid for 1 day
            userToFind.setTokenDate(new Timestamp(System.currentTimeMillis()));
            log.info("set tokenDate to user");
            userDao.save(userToFind);
            log.info("user saved");
            return userToFind;
        }catch (Exception e){
            e.printStackTrace();
            log.error("Failure: user email " + email + " doesn't exist.");
            throw new NotFoundException("L'utilisateur avec l'email " + email + " est INTROUVABLE.");
        }
    }

    /**
     * <p>finds a user by token set to reset a password</p>
     * @param token
     * @return a user
     */
    @GetMapping (value = "/Utilisateurs/MotDePasseReset")
    public ResponseEntity<User> findUserByToken(@RequestParam String token) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<User> user = searchOptionalUserByToken(token);
        User userToFind = user.get();
        log.info("returning Optional<User>");
        return new ResponseEntity<>(userToFind, HttpStatus.OK);
    }

    /**
     * <p>resets a user's password</p>
     * @param token reset token
     * @param password new password
     * @return user
     */
    @PostMapping(value = "/Utilisateurs/MotDePasseReset")
    public Optional<User> findUserByTokenAndSetsNewPassword(@RequestParam String token, @RequestParam String password) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<User> user = searchOptionalUserByToken(token);
        User resetUser = user.get();
        resetUser.setPassword(Encryption.encrypt(password));
        log.info("changing password of user and encrypts it ");
        resetUser.setResetToken(null);
        log.info("setting resettoken to null");
        resetUser.setTokenDate(null);
        log.info("setting tokendate to null");
        userDao.save(resetUser);
        log.info("user updated");
        return user;
    }

    /**
     * <p>deletes a user from db and all its datas</p>
     * @param id
     */
    @PostMapping(value = "/Utilisateurs/delete/{id}")
    public void deleteUSer(@PathVariable Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalUser(id);
        User userToDelete = userDao.getOne(id);
        userDao.delete(userToDelete);
        log.info("user deleted");
    }

    /**
     * Searches for optional User by id.
     * @param id
     */
    private Optional<User> searchOptionalUser(Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<User> user = userDao.findById(id);
        log.info("searching Optional<User> by id");
        if(!user.isPresent()) {
            log.error("Failure: user id " + id + " doesn't exist.");
            throw new NotFoundException("L'utilisateur avec l'id " + id + " est INTROUVABLE.");
        }
        return user;
    }
    /**
     * Searches for optional User by resetToken.
     * @param token
     */
    private Optional<User> searchOptionalUserByToken(String token){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<User> user = userDao.findByResetToken(token);
        log.info("searching Optional<User> by id");
        if(!user.isPresent()) {
            log.error("Failure: user token " + token + " doesn't exist.");
            throw new NotFoundException("L'utilisateur avec le token " + token+ " est INTROUVABLE.");
        }
        return user;
    }


}
