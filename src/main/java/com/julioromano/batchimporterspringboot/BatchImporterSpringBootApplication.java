package com.julioromano.batchimporterspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BatchImporterSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchImporterSpringBootApplication.class, args);
    }

}
