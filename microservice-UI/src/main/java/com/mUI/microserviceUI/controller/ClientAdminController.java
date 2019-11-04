package com.mUI.microserviceUI.controller;


import com.mUI.microserviceUI.beans.UserBean;
import com.mUI.microserviceUI.proxies.MicroserviceUsersProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Controller linking with microservice-users</h2>
 */
@Controller
public class ClientAdminController {

    @Autowired
    private MicroserviceUsersProxy usersProxy;

    /**
     * <p>Lists all users</p>
     * @param model
     * @return admin-infos.html
     */
    @RequestMapping("/Admin")
    public String adminListUsers(Model model){
        List<UserBean> users =  usersProxy.listUsers();
        model.addAttribute("users", users);

        return "admin-infos";
    }

}