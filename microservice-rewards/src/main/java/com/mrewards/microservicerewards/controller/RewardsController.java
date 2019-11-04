package com.mrewards.microservicerewards.controller;

import com.mrewards.microservicerewards.dao.RewardDao;
import com.mrewards.microservicerewards.exceptions.CannotAddException;
import com.mrewards.microservicerewards.exceptions.NotFoundException;
import com.mrewards.microservicerewards.manager.RewardsManagerImpl;
import com.mrewards.microservicerewards.model.Reward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <h2>Controller for Model Reward</h2>
 */
@RestController
public class RewardsController {
    @Autowired
    private RewardDao rewardDao;

    private RewardsManagerImpl rewardsManager = new RewardsManagerImpl();

    /**
     * <p>Lists all reward accounts</p>
     * @return a list
     */
    @GetMapping(value= "/CarteFidelites")
    public List<Reward> listRewards() {
        return rewardDao.findAll();
    }

    /**
     * <p>Adds a new reward account to db for a user.</p>
     * <p>The user needs to create a reward account per merchant.</p>
     * @param reward
     * @return responseEntity
     */
    @PostMapping(value = "/CarteFidelites/add-reward")
    public ResponseEntity<Reward> addReward(@RequestBody Reward reward) {
        Reward rewardAdded =  rewardDao.save(reward);
        if (rewardAdded == null) {throw new CannotAddException("Reward03");}
        return new ResponseEntity<Reward>(rewardAdded, HttpStatus.CREATED);
    }


    /**
     * <p>shows details of a particular reward account by its id</p>
     * @param id
     * @return the reward
     */
    @GetMapping(value = "/CarteFidelites/{id}")
    public Optional<Reward> showReward(@PathVariable Integer id) {
        Optional<Reward> reward = rewardDao.findById(id);
        if(!reward.isPresent()) {
            throw new NotFoundException("La carte avec l'id " + id + " est INTROUVABLE.");
        }
        return reward;
    }

    /**
     * <p>Adds points to an account </p>
     * @param id
     * @return
     */
    @PostMapping(value = "/CarteFidelites/{id}/add-point")
    public ResponseEntity<Reward> addPoint(@PathVariable Integer id){
        Optional<Reward> rewardGiven = rewardDao.findById(id);
        if(!rewardGiven.isPresent()) {
            throw new NotFoundException("La carte avec l'id " + id + " est INTROUVABLE.");
        }
        Reward rewardAccount = rewardDao.findRewardsById(id);
        rewardAccount = rewardsManager.addPointManager(rewardAccount);
        rewardDao.save(rewardAccount);
        return new ResponseEntity<Reward>(rewardAccount, HttpStatus.ACCEPTED);
    }





   
}
