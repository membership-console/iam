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

    /**
     * ユーザグループを作成
     * 
     * @param userGroupModel ユーザグループ
     */
    void insert(final UserGroupModel userGroupModel);

    /**
     * ユーザグループ名の存在確認
     * 
     * @param name ユーザグループ名
     * @return ユーザグループが存在するか
     */
    boolean existsByName(final String name);

    /**
     * IDからユーザグループの存在確認
     * 
     * @param id ユーザグループID
     * @return ユーザグループが存在するか
     */
    boolean existsById(final Integer id);

    /**
     * IDからユーザグループを削除
     * 
     * @param id ユーザグループID
     */
    void deleteById(final Integer id);

}
