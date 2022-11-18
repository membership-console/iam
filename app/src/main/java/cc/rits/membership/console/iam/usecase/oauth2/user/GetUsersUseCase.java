package cc.rits.membership.console.iam.usecase.oauth2.user;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.enums.Scope;
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
     * @param loginClient ログインクライアント
     * @return ユーザリスト
     */
    @Transactional
    public List<UserModel> handle(final ClientModel loginClient) {
        // スコープチェック
        if (!loginClient.hasScope(Scope.USER_READ)) {
            throw new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION);
        }

        // ユーザリストを取得
        return this.userRepository.selectAll();
    }

}
