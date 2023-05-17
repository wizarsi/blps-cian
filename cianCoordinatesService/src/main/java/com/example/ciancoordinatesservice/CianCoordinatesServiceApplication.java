package com.example.ciancoordinatesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class CianCoordinatesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CianCoordinatesServiceApplication.class, args);
    }

}
