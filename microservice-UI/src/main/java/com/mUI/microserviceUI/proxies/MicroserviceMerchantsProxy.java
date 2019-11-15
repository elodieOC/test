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

    @PostMapping(value = "microservice-merchants/Marchands/add-shop")
    MerchantBean addShop(@RequestBody MerchantBean merchantBean);

    @GetMapping( value = "microservice-merchants/Marchands/{id}")
    MerchantBean showShop(@PathVariable("id") Integer id);

    @PostMapping(value = "microservice-merchants/Marchands/delete/{id}")
    void deleteShop(@PathVariable("id") Integer id);


}
