package com.mUI.microserviceUI.controller;

import com.mUI.microserviceUI.beans.UserBean;
import com.mUI.microserviceUI.proxies.MicroserviceUsersProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class ClientController {
    @Autowired
    private MicroserviceUsersProxy usersProxy;

    @GetMapping("/")
    public String home( HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        model.addAttribute("session", session);
    return "index";
    }


    @GetMapping("/Accueil")
    public String hometwice(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        model.addAttribute("session", session);
        return "index";
    }

    @GetMapping("/APropos")
    public String about(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        model.addAttribute("session", session);
        return "about";
    }
}