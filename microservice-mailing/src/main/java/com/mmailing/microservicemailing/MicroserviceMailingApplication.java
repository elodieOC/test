package com.mmailing.microservicemailing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class MicroserviceMailingApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				MicroserviceMailingApplication.class, args);
	}



}
