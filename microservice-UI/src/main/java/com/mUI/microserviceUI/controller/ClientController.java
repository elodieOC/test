package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.proxies.MicroserviceUsersProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class ClientController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @GetMapping(value = {"/", "/Accueil"})
    public String home( HttpServletRequest request, Model model){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        model.addAttribute("session", session);
        return "index";
    }


    @GetMapping("/APropos")
    public String about(HttpServletRequest request, Model model){
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        HttpSession session = request.getSession();
        model.addAttribute("session", session);
        return "about";
    }
}