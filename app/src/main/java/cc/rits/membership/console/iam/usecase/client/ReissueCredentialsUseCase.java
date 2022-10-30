package cc.rits.membership.console.iam.usecase.client;

import java.util.Base64;

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.ClientRepository;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * 認証情報再発行ユースケース
 */
@RequiredArgsConstructor
@Component
public class ReissueCredentialsUseCase {

    private final ClientRepository clientRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param id ID
     * @return クライアント
     */
    @Transactional
    public ClientModel handle(final UserModel loginUser, final String id) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // クライアントを取得 & 存在チェック
        final var client = this.clientRepository.selectById(id) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CLIENT));

        // 認証情報を再発行
        client.setClientId(Base64.getUrlEncoder().encodeToString(KeyGenerators.secureRandom(32).generateKey()));
        client.setClientSecret(Base64.getUrlEncoder().encodeToString(KeyGenerators.secureRandom(32).generateKey()));
        this.clientRepository.updateClientIdAndSecret(client);

        return client;
    }

}
