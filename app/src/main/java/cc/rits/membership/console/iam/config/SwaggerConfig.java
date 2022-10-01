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

    private final ProjectProperty projectProperty;;

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder() //
            .group("Public API") //
            .packagesToScan("cc.rits.membership.console.iam.infrastructure.api") //
            .build();
    }

    @Bean
    public OpenAPI openAPI() {
        final var info = new Info() //
            .title("Membership Console IAM Internal API") //
            .version(this.projectProperty.getVersion());
        return new OpenAPI().info(info);
    }

}
