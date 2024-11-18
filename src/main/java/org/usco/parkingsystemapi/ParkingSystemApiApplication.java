package org.usco.parkingsystemapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ParkingSystemApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingSystemApiApplication.class, args);
    }

}
