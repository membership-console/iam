package cc.rits.membership.console.iam.domain.service;

import org.springframework.stereotype.Service;

import cc.rits.membership.console.iam.domain.repository.ClientRepository;
import cc.rits.membership.console.iam.exception.ConflictException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

/**
 * クライアントサービス
 */
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * クライアント名が使われていないことをチェック
     * 
     * @param name クライアント名
     */
    public void checkIsNameAlreadyUsed(final String name) throws ConflictException {
        if (this.clientRepository.existsByName(name)) {
            throw new ConflictException(ErrorCode.CLIENT_NAME_IS_ALREADY_USED);
        }
    }

}
