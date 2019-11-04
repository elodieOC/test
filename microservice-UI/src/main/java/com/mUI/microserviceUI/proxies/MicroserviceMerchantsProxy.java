package com.mUI.microserviceUI.proxies;

import com.mUI.microserviceUI.beans.MerchantBean;
import com.mUI.microserviceUI.config.FeignConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h2>Proxy links clientui to microservice-merchants</h2>
 */
@FeignClient(name = "zuul-server", contextId = "merchantsProxy", configuration = FeignConfig.class)
@RibbonClient(name = "microservice-merchants")
public interface MicroserviceMerchantsProxy {
    @GetMapping(value = "microservice-merchants/Marchands")
    List<MerchantBean> listMerchants();

    @PostMapping(value = "microservice-merchants/Marchands/add-merchant")
    MerchantBean addMerchant(@RequestBody MerchantBean merchantBean);

    @PostMapping(value = "microservice-merchants/Marchands/log-merchant")
    MerchantBean logMerchant(@RequestParam String email, @RequestParam String password);

    @PostMapping(value = "microservice-merchants/Marchands/forgot-password")
    MerchantBean findMerchantForPassword(@RequestParam String email);

    @GetMapping(value = "microservice-merchants/Marchands/MotDePasseReset")
    MerchantBean findMerchantByToken(@RequestParam String token);

    @PostMapping(value = "microservice-merchants/Marchands/MotDePasseReset")
    MerchantBean findMerchantByTokenAndSetsNewPassword(@RequestParam String token, @RequestParam String password);

    @GetMapping( value = "microservice-merchants/Marchands/MonProfil/{id}")
    MerchantBean showMerchant(@PathVariable("id") Integer id);

    @PostMapping(value = "microservice-merchants/Marchands/delete/{id}")
    void deleteMerchant(@PathVariable("id") Integer id);

}
