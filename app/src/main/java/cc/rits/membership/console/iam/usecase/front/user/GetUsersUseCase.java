package cc.rits.membership.console.iam.usecase.front.user;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

/**
 * ユーザリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUsersUseCase {

    private final IUserRepository userRepository;

    /**
     * Handle UseCase
     *
     * @return ユーザリスト
     */
    @Transactional
    public List<UserModel> handle() {
        // ユーザリストを取得
        return this.userRepository.selectAll();
    }

}
