package cc.rits.membership.console.iam.domain.repository;

import java.util.List;

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

}
