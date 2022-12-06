package cc.rits.membership.console.iam.infrastructure.api.controller.front;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.infrastructure.api.request.UserGroupUpsertRequest;
import cc.rits.membership.console.iam.infrastructure.api.response.UserGroupResponse;
import cc.rits.membership.console.iam.infrastructure.api.response.UserGroupsResponse;
import cc.rits.membership.console.iam.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.iam.usecase.front.user_group.*;
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

    private final GetUserGroupUseCase getUserGroupUseCase;

    private final CreateUserGroupUseCase createUserGroupUseCase;

    private final UpdateUserGroupUseCase updateUserGroupUseCase;

    private final DeleteUserGroupUseCase deleteUserGroupUseCase;

    /**
     * ユーザグループリスト取得API
     *
     * @return ユーザグループリスト
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserGroupsResponse getUserGroups() {
        final var userGroups = this.getUserGroupsUseCase.handle().stream() //
            .map(UserGroupResponse::new) //
            .toList();
        return new UserGroupsResponse(userGroups);
    }

    /**
     * ユーザグループ取得API
     *
     * @param userGroupId ユーザグループID
     * @return ユーザグループ
     */
    @GetMapping("/{user_group_id}")
    @ResponseStatus(HttpStatus.OK)
    public UserGroupResponse getUserGroup( //
        @PathVariable("user_group_id") final Integer userGroupId //
    ) {
        return new UserGroupResponse(this.getUserGroupUseCase.handle(userGroupId));
    }

    /**
     * ユーザグループ作成API
     *
     * @param loginUser ログインユーザ
     * @param requestBody ユーザグループ作成リクエスト
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserGroup( //
        final UserModel loginUser, //
        @RequestValidated @RequestBody final UserGroupUpsertRequest requestBody //
    ) {
        this.createUserGroupUseCase.handle(loginUser, requestBody);
    }

    /**
     * ユーザグループ更新API
     *
     * @param loginUser ログインユーザ
     * @param requestBody ユーザグループ更新リクエスト
     */
    @PutMapping("{user_group_id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserGroup( //
        final UserModel loginUser, //
        @PathVariable("user_group_id") final Integer userGroupId, //
        @RequestValidated @RequestBody final UserGroupUpsertRequest requestBody //
    ) {
        this.updateUserGroupUseCase.handle(loginUser, userGroupId, requestBody);
    }

    /**
     * ユーザグループ削除API
     *
     * @param loginUser ログインユーザ
     * @param userGroupId ユーザグループID
     */
    @DeleteMapping("{user_group_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserGroup( //
        final UserModel loginUser, //
        @PathVariable("user_group_id") final Integer userGroupId //
    ) {
        this.deleteUserGroupUseCase.handle(loginUser, userGroupId);
    }

}
