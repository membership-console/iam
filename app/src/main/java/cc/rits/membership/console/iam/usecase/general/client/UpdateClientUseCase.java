package cc.rits.membership.console.iam.usecase.general.client;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.ClientRepository;
import cc.rits.membership.console.iam.domain.service.ClientService;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.enums.Scope;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.exception.NotFoundException;
import cc.rits.membership.console.iam.infrastructure.api.request.ClientUpsertRequest;
import lombok.RequiredArgsConstructor;

/**
 * クライアント更新ユースケース
 */
@RequiredArgsConstructor
@Component
public class UpdateClientUseCase {

    private final ClientService clientService;

    private final ClientRepository clientRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param id ID
     * @param requestBody クライアント作成リクエスト
     */
    @Transactional
    public void handle(final UserModel loginUser, final String id, final ClientUpsertRequest requestBody) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // 更新対象クライアントを取得 & 存在チェック
        final var client = this.clientRepository.selectById(id) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CLIENT));

        // クライアント名が使われていないことをチェック
        if (!Objects.equals(client.getName(), requestBody.getName())) {
            this.clientService.checkIsNameAlreadyUsed(requestBody.getName());
        }

        // クライアントを更新
        final var scopes = requestBody.getScopes().stream() //
            .map(Scope::find) //
            .filter(Optional::isPresent) //
            .map(Optional::get) //
            .collect(Collectors.toList());
        client.setName(requestBody.getName());
        client.setScopes(scopes);
        this.clientRepository.updateNameAndScopes(client);
    }

}
