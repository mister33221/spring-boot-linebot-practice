package com.example.springbootlinebotecho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class SpringBootLineBotEchoApplication {

	public static Path downloadedContentDir ;

	public static void main(String[] args) throws IOException {

		downloadedContentDir = Files.createTempDirectory("line-bot");
		SpringApplication.run(SpringBootLineBotEchoApplication.class, args);
	}

}
