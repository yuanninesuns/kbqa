package org.horizon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.horizon.mapper")
public class KGServer {
	public static void main(String[] args) {

		SpringApplication.run(KGServer.class, args);
	}
}
