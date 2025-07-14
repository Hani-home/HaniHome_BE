package org.hanihome.hanihomebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableConfigurationProperties
@EnableAspectJAutoProxy
@EnableJpaAuditing
@SpringBootApplication
public class HaniHomeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaniHomeBeApplication.class, args);
    }

}
