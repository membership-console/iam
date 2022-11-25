package cc.rits.membership.console.iam.domain.service;

import org.springframework.stereotype.Service;

import cc.rits.membership.console.iam.domain.repository.IUserGroupRepository;
import cc.rits.membership.console.iam.exception.ConflictException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループサービス
 */
@RequiredArgsConstructor
@Service
public class UserGroupService {

    private final IUserGroupRepository userGroupRepository;

    /**
     * ユーザグループ名が使われていないことをチェック
     * 
     * @param name ユーザグループ名
     */
    public void checkIsNameAlreadyUsed(final String name) throws ConflictException {
        if (this.userGroupRepository.existsByName(name)) {
            throw new ConflictException(ErrorCode.USER_GROUP_NAME_IS_ALREADY_USED);
        }
    }

}
