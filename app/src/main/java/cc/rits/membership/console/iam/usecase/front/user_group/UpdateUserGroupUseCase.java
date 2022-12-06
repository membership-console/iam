package cc.rits.membership.console.iam.usecase.front.user_group;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.IUserGroupRepository;
import cc.rits.membership.console.iam.domain.service.UserGroupService;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.exception.NotFoundException;
import cc.rits.membership.console.iam.infrastructure.api.request.UserGroupUpsertRequest;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループ更新ユースケース
 */
@RequiredArgsConstructor
@Component
public class UpdateUserGroupUseCase {

    private final IUserGroupRepository userGroupRepository;

    private final UserGroupService userGroupService;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param userGroupId ユーザグループID
     * @param requestBody ユーザグループ作成リクエスト
     */
    @Transactional
    public void handle(final UserModel loginUser, final Integer userGroupId, final UserGroupUpsertRequest requestBody) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // 更新対象ユーザグループを取得 & 存在チェック
        final var userGroup = this.userGroupRepository.selectById(userGroupId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP));

        // ユーザグループ名が使われていないことをチェック
        if (!Objects.equals(userGroup.getName(), requestBody.getName())) {
            this.userGroupService.checkIsNameAlreadyUsed(requestBody.getName());
        }

        // ユーザグループを更新
        final var userGroupRoles = requestBody.getRoles().stream() //
            .map(Role::find) //
            .filter(Optional::isPresent) //
            .map(Optional::get) //
            .toList();
        userGroup.setName(requestBody.getName());
        userGroup.setRoles(userGroupRoles);
        this.userGroupRepository.update(userGroup);
    }

}
