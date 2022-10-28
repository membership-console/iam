package cc.rits.membership.console.iam.usecase.user;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.domain.service.UserService;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.ForbiddenException;
import cc.rits.membership.console.iam.exception.NotFoundException;
import cc.rits.membership.console.iam.infrastructure.api.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;

/**
 * ユーザ更新ユースケース
 */
@RequiredArgsConstructor
@Component
public class UpdateUserUseCase {

    private final UserRepository userRepository;

    private final UserGroupRepository userGroupRepository;

    private final UserService userService;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param userId ユーザID
     * @param requestBody ユーザ更新リクエスト
     */
    @Transactional
    public void handle(final UserModel loginUser, final Integer userId, final UserUpdateRequest requestBody) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // 更新対象ユーザを取得 & 存在チェック
        final var user = this.userRepository.selectById(userId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

        // メールアドレスが使われていないことをチェック
        if (!Objects.equals(user.getEmail(), requestBody.getEmail())) {
            this.userService.checkIsEmailAlreadyUsed(requestBody.getEmail());
        }

        // ユーザグループリストの存在チェック
        if (!this.userGroupRepository.existsByIds(requestBody.getUserGroupIds())) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP);
        }

        // ユーザを更新
        user.setFirstName(requestBody.getFirstName());
        user.setLastName(requestBody.getLastName());
        user.setEmail(requestBody.getEmail());
        user.setEntranceYear(requestBody.getEntranceYear());
        user.setUserGroups(this.userGroupRepository.selectByIds(requestBody.getUserGroupIds()));
        this.userRepository.update(user);
    }

}
