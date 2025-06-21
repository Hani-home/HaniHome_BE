package org.hanihome.hanihomebe.global.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("API 문서").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList("AccessToken( Bearer없이 토큰만 넣어주세요:) )"))
                .components(new Components()
                        .addSecuritySchemes("AccessToken( Bearer없이 토큰만 넣어주세요:) )", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")))

                // Swagger-UI Try it out → "/api/..." 상대 경로로 호출
                .addServersItem(new Server().url("/"));
    }
}
