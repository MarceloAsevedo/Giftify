package Giftify.Giftify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"Giftify"})
public class GiftifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiftifyApplication.class, args);
	}

}
