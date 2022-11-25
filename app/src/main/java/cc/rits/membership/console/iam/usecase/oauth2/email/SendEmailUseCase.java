package cc.rits.membership.console.iam.usecase.oauth2.email;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.repository.IUserRepository;
import cc.rits.membership.console.iam.enums.Scope;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.infrastructure.api.request.EmailSendRequest;
import cc.rits.membership.console.iam.util.MailUtil;
import lombok.RequiredArgsConstructor;

/**
 * メール送信ユースケース
 */
@RequiredArgsConstructor
@Component
public class SendEmailUseCase {

    private final IUserRepository userRepository;

    private final MailUtil mailUtil;

    /**
     * Handle UseCase
     *
     * @param loginClient ログインクライアント
     * @param requestBody メール送信リクエスト
     */
    @Transactional
    public void handle(final ClientModel loginClient, final EmailSendRequest requestBody) {
        // スコープチェック
        if (!loginClient.hasScope(Scope.EMAIL)) {
            throw new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION);
        }

        // 宛先リストを取得
        final var tos = this.userRepository.selectEmailsByIds(requestBody.getUserIds());

        // メールを送信
        this.mailUtil.send(tos, requestBody.getSubject(), requestBody.getBody());
    }

}
