package com.mrewards.microservicerewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceRewardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceRewardsApplication.class, args);
	}

}
