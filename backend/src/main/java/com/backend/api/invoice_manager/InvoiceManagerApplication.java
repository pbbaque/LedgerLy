package com.backend.api.invoice_manager;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@EnableCaching
@SpringBootApplication
public class InvoiceManagerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(InvoiceManagerApplication.class, args);
        Environment env = context.getEnvironment();

        System.out.println("DB URL => " + env.getProperty("spring.datasource.url"));
        System.out.println("Active Profiles => " + Arrays.toString(env.getActiveProfiles()));
	}

}
