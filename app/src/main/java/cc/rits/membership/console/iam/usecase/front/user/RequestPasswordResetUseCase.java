package cc.rits.membership.console.iam.usecase.front.user;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel;
import cc.rits.membership.console.iam.domain.repository.IPasswordResetTokenRepository;
import cc.rits.membership.console.iam.domain.repository.IUserRepository;
import cc.rits.membership.console.iam.exception.BadRequestException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.infrastructure.api.request.RequestPasswordResetRequest;
import cc.rits.membership.console.iam.property.ProjectProperty;
import cc.rits.membership.console.iam.util.MailUtil;
import lombok.RequiredArgsConstructor;

/**
 * パスワードリセット要求ユースケース
 */
@RequiredArgsConstructor
@Component
public class RequestPasswordResetUseCase {

    private final IUserRepository userRepository;

    private final IPasswordResetTokenRepository passwordResetTokenRepository;

    private final MailUtil mailUtil;

    private final ProjectProperty projectProperty;

    /**
     * Handle UseCase
     *
     * @param requestBody パスワードリセット要求リクエスト
     */
    @Transactional
    public void handle(final RequestPasswordResetRequest requestBody) {
        // ユーザの取得 & 存在チェック
        final var user = this.userRepository.selectByEmail(requestBody.getEmail()) //
            .orElseThrow(() -> new BadRequestException(ErrorCode.REQUESTED_EMAIL_IS_NOT_EXISTS));

        // パスワードリセットトークンを発行
        final var passwordResetToken = PasswordResetTokenModel.builder() //
            .userId(user.getId()) //
            .build();
        this.passwordResetTokenRepository.insert(passwordResetToken);

        // パスワード再発行メールを送信
        final var body = String.format("""
            パスワード再発行の申請を受け付けました。

            パスワードの再設定をご希望の場合は、下記URLから新しいパスワードをご登録ください。

            ▼パスワードの再設定URL
            %s/%s
            ※ URLの有効期限は30分間です
            """, this.projectProperty.getPasswordResetUrl(), passwordResetToken.getToken());
        this.mailUtil.send(List.of(user.getEmail()), "【要確認】パスワード再発行のお知らせ", body);
    }

}
