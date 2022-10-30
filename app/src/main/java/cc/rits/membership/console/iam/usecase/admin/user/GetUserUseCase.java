package cc.rits.membership.console.iam.usecase.admin.user;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.enums.Scope;
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
     * @param loginClient ログインクライアント
     * @param userId ユーザID
     * @return ユーザ
     */
    @Transactional
    public UserModel handle(final ClientModel loginClient, final Integer userId) {
        // スコープチェック
        if (!loginClient.hasScope(Scope.USER_READ)) {
            throw new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION);
        }

        // ユーザリストを取得
        return this.userRepository.selectById(userId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

}
