package cc.rits.membership.console.iam.usecase.front.client;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.repository.IClientRepository;
import lombok.RequiredArgsConstructor;

/**
 * クライアントリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetClientsUseCase {

    private final IClientRepository clientRepository;

    /**
     * Handle UseCase
     *
     * @return クライアントリストリスト
     */
    @Transactional
    public List<ClientModel> handle() {
        return this.clientRepository.selectAll();
    }

}
