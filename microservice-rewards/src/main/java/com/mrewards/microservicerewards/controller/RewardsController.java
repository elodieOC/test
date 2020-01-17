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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

    /**
     * <p>Lists all reward accounts</p>
     * @return a list
     */
    @GetMapping(value= "/CarteFidelites")
    public List<Reward> listRewards() {
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
        Reward rewardAdded =  rewardDao.save(reward);
        if (rewardAdded == null) {throw new CannotAddException("AddFail");}

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
        // create QR Code each time the page is called, not saved in db
        String data = "localhost:8080/CarteFidelites/"+reward.get().getId()+"/add-point";
        int size = 400;
        try {
            // encode
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
            e.printStackTrace();
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
        checkOptionalReward(id);
        Reward rewardAccount = rewardDao.findRewardsById(id);
        rewardAccount = rewardsManager.addPointManager(rewardAccount);
        rewardDao.save(rewardAccount);
        return new ResponseEntity<Reward>(rewardAccount, HttpStatus.ACCEPTED);
    }

    /**
     * <p>redeems reward from an account </p>
     * @param id
     * @return
     */
    @PostMapping(value = "/CarteFidelites/{id}/redeem")
    public ResponseEntity<Reward> redeem(@PathVariable Integer id){
        checkOptionalReward(id);
        Reward rewardAccount = rewardDao.findRewardsById(id);
        rewardAccount = rewardsManager.redeemRewardManager(rewardAccount);
        rewardDao.save(rewardAccount);
        return new ResponseEntity<Reward>(rewardAccount, HttpStatus.ACCEPTED);
    }

    /**
     * <p>deletes a merchant from db and all its datas</p>
     * @param id
     */
    @PostMapping(value = "/CarteFidelites/delete/{id}")
    public void deleteUSer(@PathVariable Integer id){
        Optional<Reward> user = rewardDao.findById(id);
        if(!user.isPresent()) {
            throw new NotFoundException("Le compte fidélité avec l'id " + id + " est INTROUVABLE.");
        }
        Reward rewardToDelete = rewardDao.getOne(id);
        rewardDao.delete(rewardToDelete);
    }


    private Optional<Reward> checkOptionalReward(Integer id){
        Optional<Reward> rewardGiven = rewardDao.findById(id);
        if(!rewardGiven.isPresent()) {
            throw new NotFoundException("La carte avec l'id " + id + " est INTROUVABLE.");
        }
        return rewardGiven;
    }



   
}
