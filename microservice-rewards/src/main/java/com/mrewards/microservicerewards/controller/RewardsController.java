package com.mrewards.microservicerewards.controller;

import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.mrewards.microservicerewards.dao.RewardDao;
import com.mrewards.microservicerewards.exceptions.CannotAddException;
import com.mrewards.microservicerewards.exceptions.NotFoundException;
import com.mrewards.microservicerewards.manager.RewardsManagerImpl;
import com.mrewards.microservicerewards.model.Reward;
import com.mrewards.microservicerewards.utils.QRCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.InetAddress;
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

    private QRCodeGenerator qrCodeGenerator;

    @Autowired
    Environment environment;
    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>Lists all reward accounts</p>
     * @return a list
     */
    @GetMapping(value= "/CarteFidelites")
    public List<Reward> listRewards() {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        log.info("Listing of all rewards");
        List<Reward> rewards = rewardDao.findAll();
        return rewards;
    }

    /**
     * <p>Adds a new reward account to db for a user.</p>
     * <p>The user needs to create a reward account per merchant.</p>
     * @param reward
     * @return responseEntity
     */
    @PostMapping(value = "/CarteFidelites/add-account")
    public ResponseEntity<Reward> addReward(@RequestBody Reward reward) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Reward rewardAdded =  rewardDao.save(reward);
        if (rewardAdded == null) {
            log.error("failure add fidelity card");
            throw new CannotAddException("AddFail");
        }
        log.info("fidelity card added");
        return new ResponseEntity<>(rewardAdded, HttpStatus.CREATED);
    }


    /**
     * <p>shows details of a particular reward account by its id</p>
     * @param id
     * @return the reward
     */
    @GetMapping(value = "/CarteFidelites/{id}")
    public Optional<Reward> showReward(@PathVariable Integer id) {
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<Reward> reward = searchOptionalReward(id);
        // create QR Code each time the page is called, not saved in db
        // !!!!!!!  For test purposes in Local, the host is set in the url to test qrcode
        // scanning on mobile device from the same network as the computer !!!!!!!!!!!!!!!
        //Get Host address:
        String host = "";
        try{
            host = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            host = "localhost";
            e.printStackTrace();
        }
        String data = "http://"+host+":8080/CarteFidelites/"+reward.get().getId()+"/add-point";
        int size = 395;
        try {
            // encode
            log.info("building QRCode for fidelity card");
            BitMatrix bitMatrix = qrCodeGenerator.generateMatrix(data, size);
            String imageFormat = "png";
            String nameAndPath = "C:\\code\\qrcode-01.";
            File outputFile = new File(nameAndPath);
            String outputFileName = outputFile + imageFormat;
            // write in a file
            qrCodeGenerator.writeImage(outputFileName, imageFormat, bitMatrix);
            // Load QR image
            MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
            // Convert QR image to byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( qrImage, "png", baos );
            baos.flush();
            byte[] qrImageInByte = baos.toByteArray();
            //Set QR for card
            reward.get().setQrCode(qrImageInByte);
            baos.close();
        } catch (Exception e) {
            log.error("Failure building QRCode for fidelity card");
            e.printStackTrace();
        }
        log.info("returning fidelity card");
        return reward;
    }

    /**
     * <p>Adds points to an account </p>
     * @param id id of fidelicty card
     * @return reward
     */
    @PostMapping(value = "/CarteFidelites/{id}/add-point")
    public ResponseEntity<Reward> addPoint(@PathVariable Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalReward(id);
        Reward rewardAccount = rewardDao.findRewardsById(id);
        log.info("search by id for card to add point to");
        rewardAccount = rewardsManager.addPointManager(rewardAccount);
        log.info("point added to fidelity card");
        rewardDao.save(rewardAccount);
        log.info("fidelity card updated");
        return new ResponseEntity<>(rewardAccount, HttpStatus.ACCEPTED);
    }

    /**
     * <p>redeems reward from an account </p>
     * @param id
     * @return reward
     */
    @PostMapping(value = "/CarteFidelites/{id}/redeem")
    public ResponseEntity<Reward> redeem(@PathVariable Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalReward(id);
        Reward rewardAccount = rewardDao.findRewardsById(id);
        log.info("search by id for card to add point to");
        rewardAccount = rewardsManager.redeemRewardManager(rewardAccount);
        log.info("reward redeemed from fidelity card");
        rewardDao.save(rewardAccount);
        log.info("fidelity card updated");
        return new ResponseEntity<>(rewardAccount, HttpStatus.ACCEPTED);
    }

    /**
     * <p>deletes a merchant from db and all its datas</p>
     * @param id
     */
    @PostMapping(value = "/CarteFidelites/delete/{id}")
    public void deleteUSer(@PathVariable Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        searchOptionalReward(id);
        Reward rewardToDelete = rewardDao.getOne(id);
        log.info("search by id for card to be deleted");
        rewardDao.delete(rewardToDelete);
        log.info("card deleted");
    }

    /**
     * Searches for optional Reward by id.
     * @param id
     * @return reward
     */
    private Optional<Reward> searchOptionalReward(Integer id){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        Optional<Reward> rewardGiven = rewardDao.findById(id);
        log.info("searching Optional<Reward> by id");
        if(!rewardGiven.isPresent()) {
            log.error("Failure: fidelity card id " + id + " doesn't exist.");
            throw new NotFoundException("La carte avec l'id " + id + " est INTROUVABLE.");
        }
        return rewardGiven;
    }



   
}
