package cc.rits.membership.console.iam.domain.repository;

import java.util.List;
import java.util.Optional;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;

/**
 * ユーザグループリポジトリ
 */
public interface UserGroupRepository {

    /**
     * ユーザグループリストを全件取得
     * 
     * @return ユーザグループリスト
     */
    List<UserGroupModel> selectAll();

    /**
     * IDからユーザグループを取得
     * 
     * @param userGroupId ユーザグループID
     * @return ユーザグループ
     */
    Optional<UserGroupModel> selectById(final Integer userGroupId);

}
