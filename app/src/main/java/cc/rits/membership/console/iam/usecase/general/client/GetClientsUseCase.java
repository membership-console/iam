package cc.rits.membership.console.iam.usecase.general.client;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.ClientRepository;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;

/**
 * クライアントリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetClientsUseCase {

    private final ClientRepository clientRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @return クライアントリストリスト
     */
    @Transactional
    public List<ClientModel> handle(final UserModel loginUser) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_VIEWER)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        return this.clientRepository.selectAll();
    }

}
