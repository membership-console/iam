package cc.rits.membership.console.iam.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループ取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUserGroupUseCase {

    private final UserGroupRepository userGroupRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param userGroupId ユーザグループID
     * @return ユーザグループ
     */
    @Transactional
    public UserGroupModel handle(final UserModel loginUser, final Integer userGroupId) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_VIEWER)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // ユーザグループを取得
        return this.userGroupRepository.selectById(userGroupId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP));
    }

}
