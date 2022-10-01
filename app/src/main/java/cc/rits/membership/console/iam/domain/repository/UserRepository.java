package cc.rits.membership.console.iam.domain.repository;

import cc.rits.membership.console.iam.domain.model.UserModel;

import java.util.Optional;

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

}
