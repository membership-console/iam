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
 * ユーザ取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUserUseCase {

    private final UserRepository userRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param userId ユーザID
     * @return ユーザ
     */
    @Transactional
    public UserModel handle(final UserModel loginUser, final Integer userId) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_VIEWER)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // ユーザを取得 & 存在チェック
        return this.userRepository.selectById(userId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

}
