package io.spring.cloud.sleuth.docs.service4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        new SpringApplication(Application.class).run(args);
    }
}

