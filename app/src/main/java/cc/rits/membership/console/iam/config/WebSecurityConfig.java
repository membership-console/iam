package cc.rits.membership.console.iam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import cc.rits.membership.console.iam.config.auth.ClientAuthenticationEntryPoint;
import cc.rits.membership.console.iam.config.auth.CustomUserDetailsService;
import cc.rits.membership.console.iam.config.auth.UserAccessDeniedHandler;
import cc.rits.membership.console.iam.config.auth.UserAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;

/**
 * セキュリティの設定
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    private final UserAccessDeniedHandler userAccessDeniedHandler;

    private final ClientAuthenticationEntryPoint clientAuthenticationEntryPoint;

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final JwtDecoder jwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        // CORSを無効化
        http.cors().disable();

        // CSRF対策を無効化
        http.csrf().disable();

        // アクセス許可
        http.authorizeHttpRequests() //
            // REST API
            .requestMatchers("/api/health", "/api/login", "/api/request_password_reset", "/api/password_reset").permitAll() //
            .requestMatchers("/api/oauth2/**").permitAll() //
            .requestMatchers("/api/**").hasRole("USER") //
            // 静的コンテンツ
            .requestMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**").permitAll() //
            .anyRequest().authenticated();
        http.exceptionHandling() //
            .authenticationEntryPoint(this.userAuthenticationEntryPoint) //
            .accessDeniedHandler(this.userAccessDeniedHandler);

        // リソースサーバ
        http.oauth2ResourceServer() //
            .jwt() //
            .decoder(this.jwtDecoder) //
            .and().authenticationEntryPoint(this.clientAuthenticationEntryPoint);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsService);
        authenticationProvider.setPasswordEncoder(this.passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(),
            new HttpSessionSecurityContextRepository());
    }

}
