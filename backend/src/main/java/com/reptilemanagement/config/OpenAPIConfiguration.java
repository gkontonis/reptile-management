package com.reptilemanagement.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecuritySchemes({
    @SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic"),
    @SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
})
public class OpenAPIConfiguration {

    static {
        // use Reusable Enums for Swagger generation:
        // see https://springdoc.org/#how-can-i-apply-enumasref-true-to-all-enums
        io.swagger.v3.core.jackson.ModelResolver.enumsAsRef = true;
    }

    /**
     * Custom API information provided with spring boot context values provided from {@code application.yml} file
     *
     * @param version     application version
     * @param name        application name
     * @param description application description
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI(
        @Value("${project.version:0.0.1-SNAPSHOT}") String version,
        @Value("${project.name:Reptile Management API}") String name,
        @Value("${project.description:RESTful API for managing reptile collections, feeding logs, and enclosures}") String description,
        @Value("${server.servlet.context-path:}") String serverContextPath,
        @Value("${server.port:8081}") String serverPort
    ) {
        final String contextPath = serverContextPath.isEmpty() ? "" : serverContextPath;
        final String serverUrl = "http://localhost:" + serverPort + contextPath;

        return new OpenAPI()
            .info(new Info()
                .title(name)
                .version(version)
                .description(description)

            ).servers(List.of(new Server().url(serverUrl).description("Development server")));
    }
}
