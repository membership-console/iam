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
     * @param id ユーザグループID
     * @return ユーザグループ
     */
    Optional<UserGroupModel> selectById(final Integer id);

    /**
     * IDリストからユーザグループリストを取得
     * 
     * @param ids ユーザグループIDリスト
     * @return ユーザグループリスト
     */
    List<UserGroupModel> selectByIds(final List<Integer> ids);

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
     * IDリストが全て存在することを確認
     * 
     * @param ids ユーザグループIDリスト
     * @return 全IDが存在するか
     */
    boolean existsByIds(final List<Integer> ids);

    /**
     * IDからユーザグループを削除
     * 
     * @param id ユーザグループID
     */
    void deleteById(final Integer id);

    /**
     * ユーザグループを更新
     * 
     * @param userGroupModel ユーザグループ
     */
    void update(final UserGroupModel userGroupModel);

}
