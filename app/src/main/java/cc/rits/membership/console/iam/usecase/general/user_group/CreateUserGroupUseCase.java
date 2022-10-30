package cc.rits.membership.console.iam.usecase.general.user_group;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository;
import cc.rits.membership.console.iam.domain.service.UserGroupService;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.infrastructure.api.request.UserGroupUpsertRequest;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループ作成ユースケース
 */
@RequiredArgsConstructor
@Component
public class CreateUserGroupUseCase {

    private final UserGroupRepository userGroupRepository;

    private final UserGroupService userGroupService;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param requestBody ユーザグループ作成リクエスト
     */
    @Transactional
    public void handle(final UserModel loginUser, final UserGroupUpsertRequest requestBody) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // ユーザグループ名が使われていないことをチェック
        this.userGroupService.checkIsNameAlreadyUsed(requestBody.getName());

        // ユーザグループを作成
        final var userGroupRoles = requestBody.getRoles().stream() //
            .map(Role::find) //
            .filter(Optional::isPresent) //
            .map(Optional::get) //
            .collect(Collectors.toList());
        final var userGroup = UserGroupModel.builder() //
            .name(requestBody.getName()) //
            .roles(userGroupRoles).build();
        this.userGroupRepository.insert(userGroup);
    }

}
