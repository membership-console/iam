package cc.rits.membership.console.iam.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest;
import cc.rits.membership.console.iam.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.iam.usecase.LoginUseCase;
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

}
