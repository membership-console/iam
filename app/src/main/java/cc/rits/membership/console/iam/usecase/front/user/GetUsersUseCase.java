package cc.rits.membership.console.iam.usecase.front.user;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;

/**
 * ユーザリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUsersUseCase {

    private final UserRepository userRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @return ユーザリスト
     */
    @Transactional
    public List<UserModel> handle(final UserModel loginUser) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_VIEWER)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // ユーザリストを取得
        return this.userRepository.selectAll();
    }

}
