package cc.rits.membership.console.iam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import cc.rits.membership.console.iam.config.auth.IamAuthenticationProvider;
import cc.rits.membership.console.iam.config.auth.IamUserDetailsService;
import cc.rits.membership.console.iam.config.auth.UnauthorizedAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;

/**
 * セキュリティの設定
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UnauthorizedAuthenticationEntryPoint authenticationEntryPoint;

    private final IamUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORSを無効化
        http.cors().disable();

        // CSRF対策
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // アクセス許可
        http.authorizeRequests() //
            .antMatchers("/api/health", "/api/login").permitAll() //
            .antMatchers("/api/**").hasRole("USER") //
            .antMatchers("/**").permitAll() //
            .anyRequest().authenticated() //
            .and().exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint);

        return http.build();
    }

    @Bean
    public <S extends Session> SessionRegistry sessionRegistry(final FindByIndexNameSessionRepository<S> sessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Bean
    public IamAuthenticationProvider adminAuthenticationProvider() {
        final var authenticationProvider = new IamAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsService);
        authenticationProvider.setPasswordEncoder(this.passwordEncoder);
        return authenticationProvider;
    }

}
