package cc.rits.membership.console.iam.usecase.front.user_group;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.repository.IUserGroupRepository;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUserGroupsUseCase {

    private final IUserGroupRepository userGroupRepository;

    /**
     * Handle UseCase
     *
     * @return ユーザグループリスト
     */
    @Transactional
    public List<UserGroupModel> handle() {
        // ユーザグループリストを取得
        return this.userGroupRepository.selectAll();
    }

}
