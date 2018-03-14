package com.squarebit.machinations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(Application.class)
                        .headless(false).run(args);
    }
}
