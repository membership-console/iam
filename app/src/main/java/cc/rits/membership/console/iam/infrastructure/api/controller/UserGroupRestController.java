package cc.rits.membership.console.iam.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.infrastructure.api.response.UserGroupResponse;
import cc.rits.membership.console.iam.infrastructure.api.response.UserGroupsResponse;
import cc.rits.membership.console.iam.usecase.GetUserGroupsUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループコントローラ
 */
@Tag(name = "User Group", description = "ユーザグループ")
@RestController
@RequestMapping(path = "/api/user-groups", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class UserGroupRestController {

    private final GetUserGroupsUseCase getUserGroupsUseCase;

    /**
     * ユーザグループリスト取得API
     *
     * @param loginUser ログインユーザ
     * @return ユーザグループリスト
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserGroupsResponse getUserGroups( //
        final UserModel loginUser //
    ) {
        final var userGroups = this.getUserGroupsUseCase.handle(loginUser).stream() //
            .map(UserGroupResponse::new) //
            .collect(Collectors.toList());
        return new UserGroupsResponse(userGroups);
    }

}
