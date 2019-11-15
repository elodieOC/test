package com.mUI.microserviceUI.proxies;

import com.mUI.microserviceUI.beans.RewardBean;
import com.mUI.microserviceUI.config.FeignConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <h2>Proxy links clientui to microservice-rewards</h2>
 */
@FeignClient(name = "zuul-server", contextId = "rewardsProxy", configuration = FeignConfig.class)
@RibbonClient(name = "microservice-rewards")
public interface MicroserviceRewardsProxy {
    @GetMapping(value = "microservice-rewards/CarteFidelites")
    List<RewardBean> listRewards();

    @PostMapping(value = "microservice-rewards/CarteFidelites/add-account")
    RewardBean addReward(@RequestBody RewardBean rewardBean);

    @GetMapping( value = "microservice-rewards/CarteFidelites/{id}")
    RewardBean showReward(@PathVariable("id") Integer id);

    @PostMapping(value = "microservice-rewards/CarteFidelites/{id}/add-point")
    RewardBean addPoint(@PathVariable("id") Integer id);

    @PostMapping(value = "microservice-rewards/CarteFidelites/{id}/redeem")
    RewardBean redeem(@PathVariable("id") Integer id);
    
    @PostMapping(value = "microservice-rewards/CarteFidelites/delete/{id}")
    void deleteAccount(@PathVariable("id") Integer id);

}
