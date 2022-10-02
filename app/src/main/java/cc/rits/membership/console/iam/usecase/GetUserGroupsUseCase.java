package cc.rits.membership.console.iam.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUserGroupsUseCase {

    private final UserGroupRepository userGroupRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @return ユーザグループリスト
     */
    @Transactional
    public List<UserGroupModel> handle(final UserModel loginUser) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_VIEWER)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // ユーザグループリストを取得
        return this.userGroupRepository.selectAll();
    }

}
