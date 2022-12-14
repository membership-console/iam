package cc.rits.membership.console.iam.usecase.front.client;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.IClientRepository;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * クライアント取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetClientUseCase {

    private final IClientRepository clientRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param id ID
     * @return クライアントリストリスト
     */
    @Transactional
    public ClientModel handle(final UserModel loginUser, final String id) {
        // クライアントを取得
        return this.clientRepository.selectById(id) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CLIENT));
    }

}
