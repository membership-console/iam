package cc.rits.membership.console.iam.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.infrastructure.api.request.LoginUserPasswordUpdateRequest;
import cc.rits.membership.console.iam.infrastructure.api.response.UserResponse;
import cc.rits.membership.console.iam.infrastructure.api.response.UsersResponse;
import cc.rits.membership.console.iam.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.iam.usecase.DeleteUserUseCase;
import cc.rits.membership.console.iam.usecase.GetUserUseCase;
import cc.rits.membership.console.iam.usecase.GetUsersUseCase;
import cc.rits.membership.console.iam.usecase.UpdateLoginUserPasswordUseCase;
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

    private final GetUsersUseCase getUsersUseCase;

    private final GetUserUseCase getUserUseCase;

    private final DeleteUserUseCase deleteUserUseCase;

    private final UpdateLoginUserPasswordUseCase updateLoginUserPasswordUseCase;

    /**
     * ユーザリスト取得API
     *
     * @param loginUser ログインユーザ
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UsersResponse getUsers( //
        final UserModel loginUser //
    ) {
        final var users = this.getUsersUseCase.handle(loginUser).stream() //
            .map(UserResponse::new) //
            .collect(Collectors.toList());
        return new UsersResponse(users);
    }

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

    /**
     * ユーザ取得API
     *
     * @param loginUser ログインユーザ
     * @param userId ユーザID
     */
    @GetMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser( //
        final UserModel loginUser, //
        @PathVariable("user_id") final Integer userId //
    ) {
        return new UserResponse(this.getUserUseCase.handle(loginUser, userId));
    }

    /**
     * ユーザ削除API
     *
     * @param loginUser ログインユーザ
     * @param userId ユーザID
     */
    @DeleteMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser( //
        final UserModel loginUser, //
        @PathVariable("user_id") final Integer userId //
    ) {
        this.deleteUserUseCase.handle(loginUser, userId);
    }

    /**
     * ログインユーザパスワード更新API
     *
     * @param loginUser ログインユーザ
     * @param requestBody ログインユーザパスワード更新リクエスト
     */
    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.OK)
    public void updateLoginUserPassword( //
        final UserModel loginUser, //
        @RequestValidated @RequestBody final LoginUserPasswordUpdateRequest requestBody //
    ) {
        this.updateLoginUserPasswordUseCase.handle(loginUser, requestBody);
    }

}
