package org.hanihome.hanihomebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;

@SpringBootApplication
public class HaniHomeBeApplication {

    public static void main(String[] args) {

        /*
        if (new File(".env").exists()) {

            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

            System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
            System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
            System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
            System.setProperty("SPRING_DATASOURCE_DRIVER_CLASS_NAME", dotenv.get("SPRING_DATASOURCE_DRIVER_CLASS_NAME"));

            System.setProperty("DOCKER_SPRING_DATASOURCE_URL", dotenv.get("DOCKER_SPRING_DATASOURCE_URL"));
            System.setProperty("SPRING_PROFILES_ACTIVE", dotenv.get("SPRING_PROFILES_ACTIVE"));

        }

         */


        SpringApplication.run(HaniHomeBeApplication.class, args);
    }

}
