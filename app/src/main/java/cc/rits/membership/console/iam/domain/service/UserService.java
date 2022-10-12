package cc.rits.membership.console.iam.domain.service;

import org.springframework.stereotype.Service;

import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.exception.ConflictException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

/**
 * ユーザサービス
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * メールアドレスが使われていないことをチェック
     * 
     * @param email メールアドレス
     */
    public void checkIsEmailAlreadyUsed(final String email) throws ConflictException {
        if (this.userRepository.existsByEmail(email)) {
            throw new ConflictException(ErrorCode.EMAIL_IS_ALREADY_USED);
        }
    }

}
