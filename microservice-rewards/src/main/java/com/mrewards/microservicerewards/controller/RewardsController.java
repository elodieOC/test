package com.mrewards.microservicerewards.controller;

import com.mrewards.microservicerewards.dao.RewardDao;
import com.mrewards.microservicerewards.exceptions.BadLoginPasswordException;
import com.mrewards.microservicerewards.exceptions.CannotAddException;
import com.mrewards.microservicerewards.exceptions.NotFoundException;
import com.mrewards.microservicerewards.model.Reward;
import com.mrewards.microservicerewards.utils.Encryption;
import com.mrewards.microservicerewards.utils.validators.RewardLoginValidator;
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
 * <h2>Controller for Model Reward</h2>
 */
@RestController
public class RewardController {
    @Autowired
    private RewardDao rewardDao;

    /**
     * <p>Lists all rewards</p>
     * @return a list
     */
    @GetMapping(value= "/Recompenses")
    public List<Reward> listRewards() {
        return rewardDao.findAll();
    }

    /**
     * <p>Adds a new reward account to db</p>
     * @param reward
     * @return responseEntity
     */
    @PostMapping(value = "/Recompenses/add-reward")
    public ResponseEntity<Reward> addReward(@RequestBody Reward reward) {
        Reward rewardAdded =  rewardDao.save(reward);
        if (rewardAdded == null) {throw new CannotAddException("Reward03");}
        return new ResponseEntity<Reward>(rewardAdded, HttpStatus.CREATED);
    }


    /**
     * <p>shows details of a particular reward by its id</p>
     * @param id
     * @return the reward
     */
    @GetMapping(value = "/Recompenses/{id}")
    public Optional<Reward> showReward(@PathVariable Integer id) {
        Optional<Reward> reward = rewardDao.findById(id);
        if(!reward.isPresent()) {
            throw new NotFoundException("L'marchand avec l'id " + id + " est INTROUVABLE.");
        }
        return reward;
    }




   
}
