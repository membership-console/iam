package cc.rits.membership.console.iam.usecase.front.user_group;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.repository.IUserGroupRepository;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループ取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUserGroupUseCase {

    private final IUserGroupRepository userGroupRepository;

    /**
     * Handle UseCase
     *
     * @param userGroupId ユーザグループID
     * @return ユーザグループ
     */
    @Transactional
    public UserGroupModel handle(final Integer userGroupId) {
        // ユーザグループを取得
        return this.userGroupRepository.selectById(userGroupId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP));
    }

}
