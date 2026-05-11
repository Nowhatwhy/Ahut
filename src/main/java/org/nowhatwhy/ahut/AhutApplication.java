package org.nowhatwhy.ahut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AhutApplication {

    public static void main(String[] args) {
        SpringApplication.run(AhutApplication.class, args);
    }

}
