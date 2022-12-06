package cc.rits.membership.console.iam.infrastructure.api.controller.oauth2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.infrastructure.api.response.UserResponse;
import cc.rits.membership.console.iam.infrastructure.api.response.UsersResponse;
import cc.rits.membership.console.iam.usecase.oauth2.user.GetUserUseCase;
import cc.rits.membership.console.iam.usecase.oauth2.user.GetUsersUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * ユーザコントローラ
 */
@Tag(name = "User", description = "ユーザ")
@RestController
@RequestMapping(path = "/api/oauth2/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class UserRestController {

    private final GetUsersUseCase getUsersUseCase;

    private final GetUserUseCase getUserUseCase;

    /**
     * ユーザリスト取得API
     * 
     * @param loginClient ログインクライアント
     * @return ユーザリスト
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UsersResponse getUsers( //
        final ClientModel loginClient //
    ) {
        final var users = this.getUsersUseCase.handle(loginClient).stream() //
            .map(UserResponse::new) //
            .toList();
        return new UsersResponse(users);
    }

    /**
     * ユーザ取得API
     *
     * @param loginClient ログインクライアント
     * @param userId ユーザID
     * @return ユーザ
     */
    @GetMapping("{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUsers( //
        final ClientModel loginClient, //
        @PathVariable("user_id") final Integer userId //
    ) {
        return new UserResponse(this.getUserUseCase.handle(loginClient, userId));
    }

}
