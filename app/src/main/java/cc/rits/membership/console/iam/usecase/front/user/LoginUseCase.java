package cc.rits.membership.console.iam.usecase.front.user;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest;
import cc.rits.membership.console.iam.property.AuthProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * ログインユースケース
 */
@RequiredArgsConstructor
@Component
public class LoginUseCase {

    private final AuthenticationProvider authenticationProvider;

    private final SecurityContextRepository securityContextRepository;

    private final HttpServletRequest httpServletRequest;

    private final HttpServletResponse httpServletResponse;

    private final HttpSession httpSession;

    private final AuthProperty authProperty;

    /**
     * Handle UseCase
     *
     * @param requestBody ログインリクエスト
     */
    @Transactional
    public void handle(final LoginRequest requestBody) {
        final var authentication = this.authenticationProvider
            .authenticate(new UsernamePasswordAuthenticationToken(requestBody.getEmail(), requestBody.getPassword()));

        // セッションにログイン情報を記録
        SecurityContextHolder.getContext().setAuthentication(authentication);
        this.httpSession.setMaxInactiveInterval(this.authProperty.getSessionTimeout());
        this.httpServletRequest.changeSessionId();
        this.securityContextRepository.saveContext(SecurityContextHolder.getContext(), this.httpServletRequest, this.httpServletResponse);
    }

}
