package cc.rits.membership.console.iam.usecase;

import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.config.auth.IamAuthenticationProvider;
import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest;
import lombok.RequiredArgsConstructor;

/**
 * ログインユースケース
 */
@RequiredArgsConstructor
@Component
public class LoginUseCase {

    private final IamAuthenticationProvider authenticationProvider;

    private final HttpSession httpSession;

    private final HttpServletRequest httpServletRequest;

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
        this.httpSession.setAttribute(PRINCIPAL_NAME_INDEX_NAME, authentication.getName());
        this.httpServletRequest.changeSessionId();
    }

}
