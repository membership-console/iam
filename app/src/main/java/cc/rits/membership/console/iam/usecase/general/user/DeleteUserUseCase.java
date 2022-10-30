package cc.rits.membership.console.iam.usecase.general.user;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * ユーザ削除ユースケース
 */
@RequiredArgsConstructor
@Component
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param userId ユーザID
     */
    @Transactional
    public void handle(final UserModel loginUser, final Integer userId) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // 削除対象ユーザの存在チェック
        if (!this.userRepository.existsById(userId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER);
        }

        // ユーザを削除
        this.userRepository.deleteById(userId);
    }

}
