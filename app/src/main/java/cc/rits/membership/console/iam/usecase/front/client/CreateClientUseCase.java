package cc.rits.membership.console.iam.usecase.front.client;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.IClientRepository;
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

    private final IClientRepository clientRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param requestBody クライアント作成リクエスト
     * @return クライアント
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
            .toList();
        final var client = ClientModel.builder() //
            .name(requestBody.getName()) //
            .scopes(scopes) //
            .build();
        this.clientRepository.insert(client);

        return client;
    }

}
