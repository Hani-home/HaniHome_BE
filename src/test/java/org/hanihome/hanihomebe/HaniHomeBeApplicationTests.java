package org.hanihome.hanihomebe;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HaniHomeBeApplicationTests {

    @BeforeAll
    static void loadEnv() {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        System.setProperty("SPRING_DATASOURCE_DRIVER_CLASS_NAME", dotenv.get("SPRING_DATASOURCE_DRIVER_CLASS_NAME"));

        System.setProperty("DOCKER_SPRING_DATASOURCE_URL", dotenv.get("DOCKER_SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_PROFILES_ACTIVE", dotenv.get("SPRING_PROFILES_ACTIVE"));

        System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
        System.setProperty("JWT_ACCESS_EXP", dotenv.get("JWT_ACCESS_EXP"));
        System.setProperty("JWT_REFRESH_EXP", dotenv.get("JWT_REFRESH_EXP"));



    }





    @Test
    void contextLoads() {
    }

}
