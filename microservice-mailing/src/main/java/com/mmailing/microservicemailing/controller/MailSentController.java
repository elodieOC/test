package com.mmailing.microservicemailing.controller;

import com.mmailing.microservicemailing.mailing.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 * <h2>Controller for the MailSent model</h2>
 */
@Controller
public class MailSentController {
    @Autowired
    private MailService mailService;


    @PostMapping(value = "/Utilisateurs/forgot-password")
    public void   sendLinkForPassword(@RequestParam String email, @RequestParam String token, @RequestParam String appUrl){
        String subject = "Réinitialisation Mot de Passe";
        String message = "Pour réinitialiser votre mdp, cliquer sur le lien suivant:\n" + appUrl+"/Utilisateurs/MotDePasseReset?token="+token;
        try{
            mailService.sendSimpleMessage(email, subject, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
