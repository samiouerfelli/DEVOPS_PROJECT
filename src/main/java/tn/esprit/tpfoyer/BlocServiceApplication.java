package tn.esprit.tpfoyer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BlocServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlocServiceApplication.class, args);
	}

}
