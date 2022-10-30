package cc.rits.membership.console.iam.usecase.general.user;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * ログアウトユースケース
 */
@RequiredArgsConstructor
@Component
public class LogoutUseCase {

    private final HttpSession httpSession;

    /**
     * Handle UseCase
     */
    @Transactional
    public void handle() {
        SecurityContextHolder.clearContext();
        this.httpSession.invalidate();
    }

}
