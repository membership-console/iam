package cc.rits.membership.console.iam.config.auth;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Access Denied Handler
 */
@RequiredArgsConstructor
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
        final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        final var exception = new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        this.handlerExceptionResolver.resolveException(request, response, null, exception);
    }

}
