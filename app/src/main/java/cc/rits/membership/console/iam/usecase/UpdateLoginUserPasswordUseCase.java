package cc.rits.membership.console.iam.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.exception.BadRequestException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.infrastructure.api.request.LoginUserPasswordUpdateRequest;
import cc.rits.membership.console.iam.util.AuthUtil;
import lombok.RequiredArgsConstructor;

/**
 * ログインユーザパスワード更新ユースケース
 */
@RequiredArgsConstructor
@Component
public class UpdateLoginUserPasswordUseCase {

    private final UserRepository userRepository;

    private final AuthUtil authUtil;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param requestBody ログインユーザパスワード更新リクエスト
     */
    @Transactional
    public void handle(final UserModel loginUser, final LoginUserPasswordUpdateRequest requestBody) {
        // 現在のパスワードが一致するかチェック
        if (!this.authUtil.isMatchPasswordAndHash(requestBody.getOldPassword(), loginUser.getPassword())) {
            throw new BadRequestException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        // パスワードを更新
        this.userRepository.updatePasswordById(loginUser.getId(), this.authUtil.hashingPassword(requestBody.getNewPassword()));
    }

}
