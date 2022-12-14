package cc.rits.membership.console.iam.usecase.front.user;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.IUserRepository;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * ユーザ取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUserUseCase {

    private final IUserRepository userRepository;

    /**
     * Handle UseCase
     * 
     * @param userId ユーザID
     * @return ユーザ
     */
    @Transactional
    public UserModel handle(final Integer userId) {
        // ユーザを取得 & 存在チェック
        return this.userRepository.selectById(userId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

}
