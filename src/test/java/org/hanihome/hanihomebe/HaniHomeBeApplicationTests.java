package org.hanihome.hanihomebe;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class HaniHomeBeApplicationTests {

    /* build.gradle 에 작성
    @BeforeAll
    static void setupEnv() {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env.test")
                .ignoreIfMissing()
                .load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }
     */
    @Test
    void hello() {

    }
}
