package com.stock.api.mock.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration
@EnableFeignClients
@ImportAutoConfiguration(FeignAutoConfiguration.class)
public class MockTradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockTradingApplication.class, args);
	}

}

