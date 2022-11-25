package cc.rits.membership.console.iam.usecase.front.user;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel;
import cc.rits.membership.console.iam.domain.repository.IPasswordResetTokenRepository;
import cc.rits.membership.console.iam.domain.repository.IUserRepository;
import cc.rits.membership.console.iam.exception.BadRequestException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.infrastructure.api.request.PasswordResetRequest;
import cc.rits.membership.console.iam.util.AuthUtil;
import lombok.RequiredArgsConstructor;

/**
 * パスワードリセットユースケース
 */
@RequiredArgsConstructor
@Component
public class ResetPasswordUseCase {

    private final IUserRepository userRepository;

    private final IPasswordResetTokenRepository passwordResetTokenRepository;

    private final AuthUtil authUtil;

    /**
     * Handle UseCase
     *
     * @param requestBody パスワードリセットリクエスト
     */
    @Transactional
    public void handle(final PasswordResetRequest requestBody) {
        // 有効なパスワードリセットトークンかチェック
        final var passwordResetToken = this.passwordResetTokenRepository.selectByToken(requestBody.getToken()) //
            .filter(PasswordResetTokenModel::isValid) //
            .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN));

        // パスワードをリセット
        this.userRepository.updatePasswordById(passwordResetToken.getUserId(), this.authUtil.hashingPassword(requestBody.getNewPassword()));

        // パスワードリセットトークンを削除
        this.passwordResetTokenRepository.deleteById(passwordResetToken.getId());
    }

}
