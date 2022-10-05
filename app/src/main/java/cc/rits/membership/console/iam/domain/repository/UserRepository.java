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
     * ユーザリストを全件取得
     * 
     * @return ユーザリスト
     */
    List<UserModel> selectAll();

}
