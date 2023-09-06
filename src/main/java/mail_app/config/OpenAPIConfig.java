package mail_app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    private static final String SCHEME_NAME = "basicAuth";
    private static final String SCHEME = "basic";

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI().components(new Components().addSecuritySchemes(SCHEME_NAME, getSecurityScheme())).
                addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .info(getInfo())
                .externalDocs(new ExternalDocumentation()
                        .description("Mail App API"));
    }

    private Info getInfo() {
        return new Info().title("Mail App API")
                .description("This is API for mail app.")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("https://springdoc.org"));
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme().name(SCHEME_NAME).type(SecurityScheme.Type.HTTP).scheme(SCHEME);
    }

}
