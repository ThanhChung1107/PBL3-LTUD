package com.ProjectPBL3.MegarMart;

import com.ProjectPBL3.MegarMart.Service.FileSystemStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication

public class MegarMartApplication {

	public static void main(String[] args) {

		SpringApplication.run(MegarMartApplication.class, args);

	}
	@Bean
	CommandLineRunner init(FileSystemStorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
}
