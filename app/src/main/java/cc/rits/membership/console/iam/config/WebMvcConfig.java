package cc.rits.membership.console.iam.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cc.rits.membership.console.iam.infrastructure.api.RestControllerArgumentResolver;
import lombok.RequiredArgsConstructor;

/**
 * Web MVCの設定
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final RestControllerArgumentResolver restControllerArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.restControllerArgumentResolver);
    }

}
