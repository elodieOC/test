package com.mUI.microserviceUI.proxies;

import com.mUI.microserviceUI.config.FeignConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <h2>Proxy links clientui to microservice-mailing</h2>
 */
@FeignClient(name = "zuul-server", url = "localhost:9004",contextId = "mailingProxy", configuration = FeignConfig.class)
@RibbonClient(name = "microservice-mailing")
public interface MicroserviceMailingProxy {
    @GetMapping(value = "microservice-mailing/mailing/forgot-password")
    void sendLinkForPassword(@RequestParam String email, @RequestParam String token, @RequestParam String appUrl);
}
