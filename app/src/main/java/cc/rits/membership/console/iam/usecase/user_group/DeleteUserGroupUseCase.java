package cc.rits.membership.console.iam.usecase.user_group;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.BadRequestException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループ削除ユースケース
 */
@RequiredArgsConstructor
@Component
public class DeleteUserGroupUseCase {

    private final UserGroupRepository userGroupRepository;

    private final UserRepository userRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param userGroupId ユーザグループID
     */
    @Transactional
    public void handle(final UserModel loginUser, final Integer userGroupId) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // ユーザグループの存在チェック
        if (!this.userGroupRepository.existsById(userGroupId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP);
        }

        // ユーザグループに所属するユーザが存在する場合は削除不可
        if (this.userRepository.countByUserGroupId(userGroupId) != 0) {
            throw new BadRequestException(ErrorCode.USER_GROUP_CANNOT_BE_DELETED);
        }

        // ユーザグループを削除
        this.userGroupRepository.deleteById(userGroupId);
    }

}
