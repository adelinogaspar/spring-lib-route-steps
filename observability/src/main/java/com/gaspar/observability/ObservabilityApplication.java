package com.gaspar.observability;

import com.gaspar.observability.integration.users.client.domain.UserClientResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableFeignClients
//@EnableAspectJAutoProxy
//@ComponentScan(basePackages = {"com.gaspar"})
public class ObservabilityApplication {
	//UserClientResponse userClientResponse = new UserClientResponse();
	public static void main(String[] args) {
		SpringApplication.run(ObservabilityApplication.class, args);
	}

}
