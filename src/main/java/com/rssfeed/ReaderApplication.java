package com.rssfeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReaderApplication {
    private static final Logger logger = LoggerFactory.getLogger(ReaderApplication.class);

    public static void main(String[] args) {
        logger.info("Start of ReaderApplication");
        SpringApplication.run(ReaderApplication.class, args);
    }
}
