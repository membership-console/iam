package cc.rits.membership.console.iam.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest;
import cc.rits.membership.console.iam.infrastructure.api.request.PasswordResetRequest;
import cc.rits.membership.console.iam.infrastructure.api.request.RequestPasswordResetRequest;
import cc.rits.membership.console.iam.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.iam.usecase.LoginUseCase;
import cc.rits.membership.console.iam.usecase.LogoutUseCase;
import cc.rits.membership.console.iam.usecase.RequestPasswordResetUseCase;
import cc.rits.membership.console.iam.usecase.ResetPasswordUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 認証コントローラ
 */
@Tag(name = "Auth", description = "認証")
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class AuthRestController {

    private final LoginUseCase loginUseCase;

    private final LogoutUseCase logoutUseCase;

    private final RequestPasswordResetUseCase requestPasswordResetUseCase;

    private final ResetPasswordUseCase resetPasswordUseCase;

    /**
     * ログインAPI
     *
     * @param requestBody ログインリクエスト
     */
    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public void login( //
        @RequestValidated @RequestBody final LoginRequest requestBody //
    ) {
        this.loginUseCase.handle(requestBody);
    }

    /**
     * ログアウトAPI
     */
    @PostMapping("logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout() {
        this.logoutUseCase.handle();
    }

    /**
     * パスワードリセット要求API
     * 
     * @param requestBody パスワードリセット要求リクエスト
     */
    @PostMapping("request_password_reset")
    @ResponseStatus(HttpStatus.OK)
    public void requestPasswordReset( //
        @RequestValidated @RequestBody final RequestPasswordResetRequest requestBody //
    ) {
        this.requestPasswordResetUseCase.handle(requestBody);
    }

    /**
     * パスワードリセットAPI
     * 
     * @param requestBody パスワードリセットリクエスト
     */
    @PostMapping("password_reset")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword( //
        @RequestValidated @RequestBody final PasswordResetRequest requestBody //
    ) {
        this.resetPasswordUseCase.handle(requestBody);
    }

}
