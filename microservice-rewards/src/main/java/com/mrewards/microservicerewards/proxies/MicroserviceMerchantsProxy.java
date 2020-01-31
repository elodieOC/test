package com.mrewards.microservicerewards.proxies;

import com.mrewards.microservicerewards.beans.MerchantBean;
import com.mrewards.microservicerewards.config.FeignConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <h2>Proxy links clientui to microservice-merchants</h2>
 */
@FeignClient(name = "zuul-server", url = "localhost:9004",contextId = "merchantsProxy", configuration = FeignConfig.class)
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
