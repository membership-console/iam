package cc.rits.membership.console.iam.infrastructure.api.controller.admin;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.infrastructure.api.response.UserResponse;
import cc.rits.membership.console.iam.infrastructure.api.response.UsersResponse;
import cc.rits.membership.console.iam.usecase.admin.user.GetUsersUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * ユーザコントローラ
 */
@Tag(name = "User", description = "ユーザ")
@RestController
@RequestMapping(path = "/api/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class UserRestController {

    private final GetUsersUseCase getUsersUseCase;

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
            .collect(Collectors.toList());
        return new UsersResponse(users);
    }

}
