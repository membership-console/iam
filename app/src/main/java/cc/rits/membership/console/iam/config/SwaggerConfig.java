package cc.rits.membership.console.iam.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cc.rits.membership.console.iam.annotation.SwaggerHiddenParameter;
import cc.rits.membership.console.iam.property.ProjectProperty;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;

/**
 * Swaggerの設定
 */
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(SwaggerHiddenParameter.class);
    }

    private final ProjectProperty projectProperty;

    @Bean
    public GroupedOpenApi frontApi() {
        return GroupedOpenApi.builder() //
            .group("Front API") //
            .packagesToScan("cc.rits.membership.console.iam.infrastructure.api.controller.front") //
            .build();
    }

    @Bean
    public GroupedOpenApi oauth2Api() {
        return GroupedOpenApi.builder() //
            .group("Admin API") //
            .packagesToScan("cc.rits.membership.console.iam.infrastructure.api.controller.oauth2") //
            .build();
    }

    @Bean
    public OpenAPI openAPI() {
        final var info = new Info() //
            .title("IAM Internal API") //
            .version(this.projectProperty.getVersion());
        return new OpenAPI().info(info);
    }

}
