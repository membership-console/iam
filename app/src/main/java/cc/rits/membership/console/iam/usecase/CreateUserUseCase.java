package cc.rits.membership.console.iam.usecase;

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
import cc.rits.membership.console.iam.infrastructure.api.request.UserCreateRequest;
import cc.rits.membership.console.iam.util.AuthUtil;
import lombok.RequiredArgsConstructor;

/**
 * ユーザ作成ユースケース
 */
@RequiredArgsConstructor
@Component
public class CreateUserUseCase {

    private final UserRepository userRepository;

    private final UserGroupRepository userGroupRepository;

    private final UserService userService;

    private final AuthUtil authUtil;

    /**
     * Handle UseCase
     *
     * @param loginUser ログインユーザ
     * @param requestBody ユーザ作成リクエスト
     */
    @Transactional
    public void handle(final UserModel loginUser, final UserCreateRequest requestBody) {
        // ロールチェック
        if (!loginUser.hasRole(Role.IAM_ADMIN)) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // メールアドレスが使われていないことをチェック
        this.userService.checkIsEmailAlreadyUsed(requestBody.getEmail());

        // ユーザグループの存在チェック
        if (!this.userGroupRepository.existsByIds(requestBody.getUserGroupIds())) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP);
        }

        // ユーザを作成
        final var user = UserModel.builder() //
            .firstName(requestBody.getFirstName()) //
            .lastName(requestBody.getLastName()) //
            .email(requestBody.getEmail()) //
            .password(this.authUtil.hashingPassword(requestBody.getPassword())) //
            .entranceYear(requestBody.getEntranceYear()) //
            .userGroups(this.userGroupRepository.selectByIds(requestBody.getUserGroupIds())) //
            .build();
        this.userRepository.insert(user);
    }

}
