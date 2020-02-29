package com.mmerchants.microservicemerchants.proxies;

import com.mmerchants.microservicemerchants.config.FeignConfig;
import com.mmerchants.microservicemerchants.model.RewardBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

/**
 * <h2>Proxy links  to microservice-rewards</h2>
 */
@FeignClient(name = "zuul-server", url = "localhost:9004",contextId = "rewardsProxy", configuration = FeignConfig.class)
@RibbonClient(name = "microservice-rewards")
public interface MicroserviceRewardsProxy {
    @GetMapping(value = "microservice-rewards/CarteFidelites")
    List<RewardBean> listRewards();

    @GetMapping( value = "microservice-rewards/CarteFidelites/{id}")
    RewardBean showReward(@PathVariable("id") Integer id);
    
    @PostMapping(value = "microservice-rewards/CarteFidelites/delete/{id}")
    void deleteAccount(@PathVariable("id") Integer id);

}
