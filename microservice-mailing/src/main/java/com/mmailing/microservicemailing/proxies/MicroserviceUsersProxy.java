package com.mmailing.microservicemailing.proxies;

import com.mmailing.microservicemailing.beans.UserBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h2>Proxy links mailing to microservice-users</h2>
 */
@FeignClient(name = "zuul-server",url = "localhost:9004", contextId = "usersProxyForMailing")
@RibbonClient(name = "microservice-users")
public interface MicroserviceUsersProxy {
    @GetMapping(value = "microservice-users/Utilisateurs")
    List<UserBean> listUsers();

    @PostMapping(value = "microservice-users/Utilisateurs/add-user")
    UserBean addUser(@RequestBody UserBean userBean);

    @PostMapping(value = "microservice-users/Utilisateurs/log-user")
    UserBean logUser(@RequestParam String email, @RequestParam String password);


    @PostMapping(value = "microservice-users/Utilisateurs/forgot-password")
    UserBean findUserForPassword(@RequestParam ("email") String email);

    @GetMapping(value = "microservice-users/Utilisateurs/MotDePasseReset")
    UserBean findUserByToken(@RequestParam String token);

    @GetMapping( value = "microservice-users/Utilisateurs/MonProfil/{id}")
    UserBean showUser(@PathVariable("id") Integer id);

}
