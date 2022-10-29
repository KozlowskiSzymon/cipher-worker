package com.example.cypherworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CypherWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CypherWorkerApplication.class, args);
    }

}
