package com.stock.api.mock.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MockTradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockTradingApplication.class, args);
	}

}

