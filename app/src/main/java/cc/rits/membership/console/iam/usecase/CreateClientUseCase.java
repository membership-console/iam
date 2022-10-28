package cc.rits.membership.console.iam.usecase;

import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.ClientRepository;
import cc.rits.membership.console.iam.domain.service.ClientService;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.enums.Scope;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.infrastructure.api.request.ClientUpsertRequest;
import lombok.RequiredArgsConstructor;

/**
 * クライアント作成ユースケース
 */
@RequiredArgsConstructor
@Component
public class CreateClientUseCase {

    private final ClientService clientService;

    private final ClientRepository clientRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param requestBody クライアント作成リクエスト
     */
    @Transactional
    public ClientModel handle(final UserModel loginUser, final ClientUpsertRequest requestBody) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // クライアント名が使われていないことをチェック
        this.clientService.checkIsNameAlreadyUsed(requestBody.getName());

        // クライアントを作成
        final var scopes = requestBody.getScopes().stream() //
            .map(Scope::find) //
            .filter(Optional::isPresent) //
            .map(Optional::get) //
            .collect(Collectors.toList());
        final var client = ClientModel.builder() //
            .name(requestBody.getName()) //
            .clientId(Base64.getUrlEncoder().encodeToString(KeyGenerators.secureRandom(32).generateKey())) //
            .clientSecret(Base64.getUrlEncoder().encodeToString(KeyGenerators.secureRandom(32).generateKey())) //
            .scopes(scopes) //
            .build();
        this.clientRepository.insert(client);

        return client;
    }

}
