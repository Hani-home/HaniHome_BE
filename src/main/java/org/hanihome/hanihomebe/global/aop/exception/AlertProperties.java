package org.hanihome.hanihomebe.global.aop.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter @Setter
@ConfigurationProperties(prefix = "admin.alert")
@Component
public class AlertProperties {
    /**
     * application.yml 의 admin.alert.emails 를 바인딩
     */
    private List<String> emails;

}

