package cn.msa.msa_museum_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MsaMuseumServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsaMuseumServerApplication.class, args);
	}

}
