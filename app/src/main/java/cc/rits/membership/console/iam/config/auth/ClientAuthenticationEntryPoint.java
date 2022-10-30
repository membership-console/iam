package cc.rits.membership.console.iam.config.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

/**
 * Authentication Entry Point
 */
@RequiredArgsConstructor
@Component
public class ClientAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence( //
        final HttpServletRequest request, //
        final HttpServletResponse response, //
        final AuthenticationException authException //
    ) {
        final var exception = new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        this.handlerExceptionResolver.resolveException(request, response, null, exception);
    }

}
