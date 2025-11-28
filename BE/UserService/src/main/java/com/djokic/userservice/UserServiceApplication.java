package com.djokic.userservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        map(dotenv, "SPRING_DATASOURCE_URL", "spring.datasource.url");
        map(dotenv, "SPRING_DATASOURCE_USERNAME", "spring.datasource.username");
        map(dotenv, "SPRING_DATASOURCE_PASSWORD", "spring.datasource.password");
        map(dotenv, "HMACSHA256_KEY", "security.jwt.secret-key");

        SpringApplication.run(UserServiceApplication.class, args);
    }

    private static void map(Dotenv dotenv, String envKey, String springPropKey) {
        String value = dotenv.get(envKey);
        if (value != null && !value.isBlank()) {
            System.setProperty(springPropKey, value);
        }
    }
}
