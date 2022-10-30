package cc.rits.membership.console.iam.usecase.general.client;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.ClientRepository;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * クライアント削除ユースケース
 */
@RequiredArgsConstructor
@Component
public class DeleteClientUseCase {

    private final ClientRepository clientRepository;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param id ID
     */
    @Transactional
    public void handle(final UserModel loginUser, final String id) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // クライアントの存在チェック
        if (!this.clientRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_CLIENT);
        }

        // クライアントを削除
        this.clientRepository.deleteById(id);
    }

}
