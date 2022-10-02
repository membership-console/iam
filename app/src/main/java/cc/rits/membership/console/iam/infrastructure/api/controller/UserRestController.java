package cc.rits.membership.console.iam.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.infrastructure.api.response.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * ユーザコントローラ
 */
@Tag(name = "User", description = "ユーザ")
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class UserRestController {

    /**
     * ログインユーザ取得API
     *
     * @param loginUser ログインユーザ
     */
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getLoginUser( //
        final UserModel loginUser //
    ) {
        return new UserResponse(loginUser);
    }

}
