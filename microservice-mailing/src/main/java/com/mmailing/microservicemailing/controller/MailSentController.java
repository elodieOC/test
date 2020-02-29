package com.mmailing.microservicemailing.controller;

import com.mmailing.microservicemailing.mailing.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 * <h2>Controller for the MailSent model</h2>
 */
@RestController
public class MailSentController {
    @Autowired
    private MailService mailService;
    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Sends a link to user to reset the forgotten password
     * @param email of user
     * @param token of user
     * @param appUrl url to reset password
     */
    @GetMapping(value = "/mailing/forgot-password")
    public void sendLinkForPassword(@RequestParam String email, @RequestParam String token, @RequestParam String appUrl){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        String subject = "Réinitialisation Mot de Passe";
        String message = "Pour réinitialiser votre mdp, cliquer sur le lien suivant:\n" + appUrl+"/Utilisateurs/MotDePasseReset?token="+token;
        try{
            mailService.sendSimpleMessage(email, subject, message);
            log.info("envoi du mail de réinitialisation de mot de passe");
        }catch (Exception e){
            log.error("error mailing");
            e.printStackTrace();
            log.error("erreur dans l'envoi du mail de réinitialisation de mot de passe");
        }
    }
}
