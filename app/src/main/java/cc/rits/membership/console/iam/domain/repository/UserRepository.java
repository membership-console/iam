package cc.rits.membership.console.iam.domain.repository;

import java.util.List;
import java.util.Optional;

import cc.rits.membership.console.iam.domain.model.UserModel;

/**
 * ユーザリポジトリ
 */
public interface UserRepository {

    /**
     * メールアドレスからユーザを取得
     *
     * @param email メールアドレス
     * @return ユーザ
     */
    Optional<UserModel> selectByEmail(final String email);

    /**
     * IDからユーザを取得
     * 
     * @param id ユーザID
     * @return ユーザ
     */
    Optional<UserModel> selectById(final Integer id);

    /**
     * ユーザリストを全件取得
     * 
     * @return ユーザリスト
     */
    List<UserModel> selectAll();

    /**
     * IDからユーザを削除
     * 
     * @param id ユーザID
     */
    void deleteById(final Integer id);

    /**
     * IDからユーザの存在確認
     * 
     * @param id ユーザID
     * @return 存在するか
     */
    boolean existsById(final Integer id);

    /**
     * ユーザグループIDからユーザ数を取得
     * 
     * @param userGroupId ユーザグループID
     * @return ユーザ数
     */
    Integer countByUserGroupId(final Integer userGroupId);

}
