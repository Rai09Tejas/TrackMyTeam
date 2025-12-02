package com.trackmyteam.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrackMyTeamApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackMyTeamApplication.class, args);
    }

}
