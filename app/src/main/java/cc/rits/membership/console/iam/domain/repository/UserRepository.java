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
     * IDリストからメールアドレスを取得
     * 
     * @param ids ユーザIDリスト
     * @return メールアドレスリスト
     */
    List<String> selectEmailsByIds(final List<Integer> ids);

    /**
     * ユーザリストを全件取得
     * 
     * @return ユーザリスト
     */
    List<UserModel> selectAll();

    /**
     * ユーザを作成
     * 
     * @param userModel ユーザ
     */
    void insert(final UserModel userModel);

    /**
     * ユーザを更新
     *
     * @param userModel ユーザ
     */
    void update(final UserModel userModel);

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
     * メールアドレスからユーザの存在確認
     * 
     * @param email メールアドレス
     * @return 存在するか
     */
    boolean existsByEmail(final String email);

    /**
     * ユーザグループIDからユーザ数を取得
     * 
     * @param userGroupId ユーザグループID
     * @return ユーザ数
     */
    Integer countByUserGroupId(final Integer userGroupId);

    /**
     * パスワードを更新
     * 
     * @param id ユーザID
     * @param password パスワード
     */
    void updatePasswordById(final Integer id, final String password);

}
